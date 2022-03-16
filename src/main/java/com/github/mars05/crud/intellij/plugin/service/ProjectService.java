package com.github.mars05.crud.intellij.plugin.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.intellij.plugin.dto.*;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.enums.FileTemplateTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.Table;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.SqlUtils;
import com.github.mars05.crud.intellij.plugin.util.TemplateUtils;
import com.github.mars05.crud.intellij.plugin.util.ValidateUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectTemplateService projectTemplateService = new ProjectTemplateService();
    private DataSourceService dataSourceService = new DataSourceService();

    public ProjectRespDTO generateProject(ProjectGenerateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        ProjectTemplateRespDTO projectTemplateRespDTO = getProjectTemplate(reqDTO.getPtId());

        ProjectRespDTO projectRespDTO = new ProjectRespDTO();
        projectRespDTO.setProjectName(reqDTO.getProjectName());
        projectRespDTO.setFiles(new Vector<>());

        TemplateParam templateParam = new TemplateParam();
        templateParam.setProjectName(reqDTO.getProjectName());
        templateParam.setBasePackage(reqDTO.getBasePackage());
        templateParam.setBasePackageDir(StringUtils.replace(reqDTO.getBasePackage(), ".", "/"));
        //Maven参数
        templateParam.setMaven(BeanUtils.convertBean(reqDTO, TemplateParam.Maven.class));
        //数据源参数
        templateParam.setDataSource(BeanUtils.convertBean(dataSourceService.detail(reqDTO.getDsId()), TemplateParam.DataSource.class));

        for (FileTemplateDTO fileTemplateRespDTO : projectTemplateRespDTO.getFileTemplateList()) {
            if (FileTemplateTypeEnum.GENERAL.getCode() == fileTemplateRespDTO.getType()) {
                FileRespDTO fileRespDTO = new FileRespDTO();
                try {
                    fileRespDTO.setPath(TemplateUtils.processTemplate(fileTemplateRespDTO.getPath(), templateParam));
                } catch (Exception e) {
                    throw new BizException("文件路径解析失败: " + fileTemplateRespDTO.getPath());
                }
                fileRespDTO.setContent(TemplateUtils.processTemplateOfNoError(fileTemplateRespDTO.getContent(), templateParam));
                projectRespDTO.getFiles().add(fileRespDTO);
            }
        }
        CodeGenerateReqDTO codeGenerateReqDTO = BeanUtils.convertBean(reqDTO, CodeGenerateReqDTO.class);
        codeGenerateReqDTO.setNameList(projectTemplateRespDTO.getFileTemplateList().stream().map(FileTemplateDTO::getName).collect(Collectors.toList()));
        projectRespDTO.getFiles().addAll(this.generateCode(codeGenerateReqDTO));
        return projectRespDTO;
    }

    public List<FileRespDTO> generateCode(CodeGenerateReqDTO reqDTO) {
        List<FileRespDTO> files = new ArrayList<>();
        if (CollectionUtils.isEmpty(reqDTO.getNameList()) || (StringUtils.isBlank(reqDTO.getDdl()) &&
                CollectionUtils.isEmpty(reqDTO.getTables()))) {
            return files;
        }
        ValidateUtils.validAnnotation(reqDTO);
        ProjectTemplateRespDTO projectTemplateRespDTO = getProjectTemplate(reqDTO.getPtId());

        TemplateParam templateParam = new TemplateParam();
        templateParam.setBasePackage(reqDTO.getBasePackage());
        templateParam.setBasePackageDir(StringUtils.replace(reqDTO.getBasePackage(), ".", "/"));

        List<Table> tables;
        if (StringUtils.isNotBlank(reqDTO.getDdl())) {
            tables = SqlUtils.getTablesByDdl(reqDTO.getDdl(), DatabaseTypeEnum.MYSQL);
        } else {
            tables = new Vector<>();
            CountDownLatch latch = new CountDownLatch(reqDTO.getTables().size());
            for (String tableName : reqDTO.getTables()) {
                // 线程处理
                new Thread(() -> {
                    try {
                        tables.add(dataSourceService.getTable(reqDTO.getDsId(), reqDTO.getDatabase(), tableName));
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            try {
                if (!latch.await(10L, TimeUnit.SECONDS)) {
                    throw new BizException("获取" + reqDTO.getTables().size() + "个表失败");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<String> errorList = reqDTO.getTables().stream().filter(name -> !tables.stream().map(Table::getTableName)
                    .collect(Collectors.toList()).contains(name)
            ).collect(Collectors.toList());
            if (!errorList.isEmpty()) {
                throw new BizException(errorList + "表获取失败");
            }
        }

        for (FileTemplateDTO fileTemplateRespDTO : projectTemplateRespDTO.getFileTemplateList()) {
            if (FileTemplateTypeEnum.CODE.getCode() == fileTemplateRespDTO.getType() && reqDTO.getNameList()
                    .contains(fileTemplateRespDTO.getName())) {

                for (Table table : tables) {
                    templateParam.setTable(table);
                    FileRespDTO fileRespDTO = new FileRespDTO();
                    try {
                        fileRespDTO.setPath(TemplateUtils.processTemplate(fileTemplateRespDTO.getPath(), templateParam));
                    } catch (Exception e) {
                        throw new BizException("文件路径解析失败: " + fileTemplateRespDTO.getPath());
                    }
                    fileRespDTO.setContent(TemplateUtils.processTemplateOfNoError(fileTemplateRespDTO.getContent(), templateParam));
                    files.add(fileRespDTO);
                }
            }
        }
        return files;
    }

    private ProjectTemplateRespDTO getProjectTemplate(Long id) {
        ProjectTemplateRespDTO projectTemplateRespDTO = projectTemplateService.detail(id);
        Preconditions.checkNotNull(projectTemplateRespDTO, "项目模板不存在");
        return projectTemplateRespDTO;
    }

    public void processProjectToDisk(ProjectRespDTO projectRespDTO, String parentDir) {
        String projectPath = parentDir + "/" + projectRespDTO.getProjectName();
//        File dir = new File(projectPath);
//        if (dir.exists()) {
//            throw new BizException("项目已存在: " + dir.getPath());
//        }
//        if (!dir.mkdirs()) {
//            throw new BizException("项目创建失败: " + dir.getPath());
//        }
        this.processCodeToDisk(projectPath, projectRespDTO.getFiles());
    }

    public void processCodeToDisk(String projectPath, List<FileRespDTO> files) {
        for (FileRespDTO fileRespDTO : files) {
            String filePath = projectPath + "/" + StringUtils.removeStart(fileRespDTO.getPath(), "/");
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new BizException("目录创建失败: " + parentFile.getPath());
            }
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(fileRespDTO.getContent());
                fileWriter.close();
            } catch (IOException e) {
                throw new BizException(e.getMessage(), e);
            }
        }
    }
}

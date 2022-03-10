package com.github.mars05.crud.intellij.plugin.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.mars05.crud.intellij.plugin.dto.*;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.enums.FileTemplateTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.param.Table;
import com.github.mars05.crud.intellij.plugin.dto.FileTemplateDTO;
import com.github.mars05.crud.intellij.plugin.util.*;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectTemplateService projectTemplateService = new ProjectTemplateService();

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
        templateParam.setMaven(BeanUtils.convertBean(reqDTO.getMaven(), TemplateParam.Maven.class));
        //数据源参数
        templateParam.setDataSource(BeanUtils.convertBean(reqDTO.getDataSource(), TemplateParam.DataSource.class));

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
        if (CollectionUtils.isEmpty(reqDTO.getNameList()) || StringUtils.isBlank(reqDTO.getDdl())) {
            return files;
        }
        ValidateUtils.validAnnotation(reqDTO);
        ProjectTemplateRespDTO projectTemplateRespDTO = getProjectTemplate(reqDTO.getPtId());

        TemplateParam templateParam = new TemplateParam();
        templateParam.setBasePackage(reqDTO.getBasePackage());
        templateParam.setBasePackageDir(StringUtils.replace(reqDTO.getBasePackage(), ".", "/"));

        for (FileTemplateDTO fileTemplateRespDTO : projectTemplateRespDTO.getFileTemplateList()) {
            if (FileTemplateTypeEnum.CODE.getCode() == fileTemplateRespDTO.getType() && reqDTO.getNameList()
                    .contains(fileTemplateRespDTO.getName())) {
                List<Table> tables = SqlUtils.getTablesByDdl(reqDTO.getDdl(), DatabaseTypeEnum.MYSQL);
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
        File dir = new File(projectPath);
        if (dir.exists()) {
            throw new BizException("项目已存在: " + dir.getPath());
        }
        if (!dir.mkdirs()) {
            throw new BizException("项目创建失败: " + dir.getPath());
        }
        for (FileRespDTO fileRespDTO : projectRespDTO.getFiles()) {
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

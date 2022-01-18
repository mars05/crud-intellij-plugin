package com.github.mars05.crud.intellij.plugin.service;

import com.github.mars05.crud.intellij.plugin.dto.*;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.param.Table;
import com.github.mars05.crud.intellij.plugin.util.Constants;
import com.github.mars05.crud.intellij.plugin.util.TemplateUtils;
import com.github.mars05.crud.intellij.plugin.util.ValidateUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ProjectService {
    private ProjectTemplateService projectTemplateService = new ProjectTemplateService();
    private FileTemplateService fileTemplateService = new FileTemplateService();
    private DataSourceService dataSourceService = new DataSourceService();

    public ProjectRespDTO generateProject(ProjectGenerateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        ProjectTemplateRespDTO projectTemplateRespDTO = projectTemplateService.detail(reqDTO.getProjectTemplateId());
        Preconditions.checkNotNull(projectTemplateRespDTO, "项目模板不存在");
        DataSourceRespDTO dataSourceRespDTO = dataSourceService.detail(reqDTO.getDsId());
        Preconditions.checkNotNull(dataSourceRespDTO, "数据源不存在");

        ProjectRespDTO projectRespDTO = new ProjectRespDTO();
        projectRespDTO.setProjectName(reqDTO.getProjectName());
        projectRespDTO.setFiles(new Vector<>());

        Map<String, Object> commonParam = new HashMap<>();
        /*项目 参数*/
        Map<String, Object> projectParam = new HashMap<>();
        projectParam.put("projectName", reqDTO.getProjectName());
        projectParam.put("basePackage", reqDTO.getBasePackage());
        projectParam.put("basePackageDir", StringUtils.replace(reqDTO.getBasePackage(), ".", "/"));
        /*数据库 参数*/
        Map<String, Object> dataSourceParam = new HashMap<>();
        dataSourceParam.put("host", dataSourceRespDTO.getHost());
        dataSourceParam.put("port", dataSourceRespDTO.getPort());
        dataSourceParam.put("username", dataSourceRespDTO.getUsername());
        dataSourceParam.put("password", dataSourceRespDTO.getPassword());
        dataSourceParam.put("catalog", reqDTO.getCatalog());
        dataSourceParam.put("schema", reqDTO.getSchema());
        /*Maven 参数*/
        Map<String, Object> mavenParam = new HashMap<>();
        mavenParam.put("groupId", reqDTO.getGroupId());
        mavenParam.put("artifactId", reqDTO.getArtifactId());
        mavenParam.put("version", reqDTO.getVersion());

        commonParam.put("project", projectParam);
        commonParam.put("dataSource", dataSourceParam);
        commonParam.put("maven", mavenParam);

        for (FileTemplateRespDTO fileTemplateRespDTO : projectTemplateRespDTO.getFileTemplateList()) {
            if (Constants.FILE_TEMPLATE_TYPE_CODE == fileTemplateRespDTO.getType()) {
                List<String> tableNames = reqDTO.getTableNames();
                if (tableNames == null) {
                    continue;
                }
                for (String tableName : tableNames) {
                    Map<String, Object> codeParam = new HashMap<>(commonParam);
                    Table table = dataSourceService.getTable(reqDTO.getDsId(), reqDTO.getCatalog(), tableName);
                    codeParam.put("table", table);
                    FileRespDTO fileRespDTO = new FileRespDTO();
                    fileRespDTO.setPath(TemplateUtils.processTemplate(fileTemplateRespDTO.getPath(), codeParam));
                    fileRespDTO.setContent(TemplateUtils.processTemplate(fileTemplateRespDTO.getContent(), codeParam));
                    projectRespDTO.getFiles().add(fileRespDTO);
                }
            } else {
                Map<String, Object> generalParam = new HashMap<>(commonParam);
                FileRespDTO fileRespDTO = new FileRespDTO();
                fileRespDTO.setPath(TemplateUtils.processTemplate(fileTemplateRespDTO.getPath(), generalParam));
                fileRespDTO.setContent(TemplateUtils.processTemplate(fileTemplateRespDTO.getContent(), generalParam));
                projectRespDTO.getFiles().add(fileRespDTO);
            }
        }
        return projectRespDTO;
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

package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProjectGenerateReqDTO {
    /**
     * 项目模板ID
     */
    @NotEmpty
    private String projectTemplateId;
    /**
     * 项目名称
     */
    @NotEmpty
    private String projectName;
    @NotEmpty
    private String basePackage;

    /*数据库 参数*/
    private String dsId;
    private String catalog;
    private String schema;
    private List<String> tableNames;

    /*Maven 参数*/
    private String groupId;
    private String artifactId;
    private String version;

}

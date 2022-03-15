package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
public class GenerateDTO {

    private Long ptId;

    private List<String> nameList;

    private String basePackage;

    private String ddl;

    private Long dsId;
    private String database;
    private String schema;
    private List<String> tables;

    private String projectName;

    private String groupId;
    private String artifactId;
    private String version;

    private boolean ddlSelected;
    private boolean codeGenerate;
}
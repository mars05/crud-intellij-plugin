package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.hub.common.model.Table;
import lombok.Data;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
public class GenerateDTO {

    private Long ptId;

    private List<String> nameList;

    private String projectPath;

    private String basePackage;

    private Long dsId;
    private String database;
    private String schema;
    private List<String> tables;
    private String ddl;
    private List<Table> modelTables;

    private String projectName;

    private String groupId;
    private String artifactId;
    private String version;

    private boolean ddlSelected;
    private boolean codeGenerate;
}
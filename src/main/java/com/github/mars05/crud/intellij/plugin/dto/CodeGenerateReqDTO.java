package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
@Accessors(chain = true)
public class CodeGenerateReqDTO {

    private Long ptId;

    private List<String> nameList;

    private String projectPath;
    private String basePackage;

    private String ddl;

    private Long dsId;
    private String database;
    private String schema;
    private List<String> tables;

}

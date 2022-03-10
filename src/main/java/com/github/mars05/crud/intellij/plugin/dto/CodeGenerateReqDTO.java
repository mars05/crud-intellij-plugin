package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
public class CodeGenerateReqDTO {

    private Long ptId;

    private List<String> nameList;

    private String basePackage;

    private String ddl;

}

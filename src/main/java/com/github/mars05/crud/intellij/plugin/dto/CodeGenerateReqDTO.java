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

    private String basePackage;

    private String ddl;

}

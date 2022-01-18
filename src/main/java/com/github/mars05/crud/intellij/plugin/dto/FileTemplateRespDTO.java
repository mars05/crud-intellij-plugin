package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileTemplateRespDTO {
    private String id;
    private String path;
    private String content;
    private String projectTemplateId;
    /**
     * 模板类型 1:普通模板 2:代码模板
     */
    private Integer type;
}

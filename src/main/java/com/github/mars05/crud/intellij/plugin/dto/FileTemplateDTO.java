package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

/**
 * @author xiaoyu
 */
@Data
public class FileTemplateDTO {

    private String name;

    private String path;

    private String content;

    private Integer type;

}

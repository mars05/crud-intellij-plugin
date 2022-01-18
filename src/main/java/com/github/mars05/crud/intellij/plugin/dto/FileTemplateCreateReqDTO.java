package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileTemplateCreateReqDTO {
    private String id;
    private String path;
    private String content;
}

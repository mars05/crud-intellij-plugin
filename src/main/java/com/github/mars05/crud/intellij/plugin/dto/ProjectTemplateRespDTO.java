package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProjectTemplateRespDTO {
    private String id;
    private String name;
    private Integer projectType;
    private List<FileTemplateRespDTO> fileTemplateList;
}

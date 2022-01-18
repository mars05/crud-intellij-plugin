package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectTemplateUpdateReqDTO extends ProjectTemplateCreateReqDTO {
    private String id;
}

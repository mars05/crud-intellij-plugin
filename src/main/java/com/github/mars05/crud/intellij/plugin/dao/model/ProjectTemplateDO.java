package com.github.mars05.crud.intellij.plugin.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ProjectTemplateDO extends BaseDO {
    private String id;
    private String name;
    private Integer projectType;
}

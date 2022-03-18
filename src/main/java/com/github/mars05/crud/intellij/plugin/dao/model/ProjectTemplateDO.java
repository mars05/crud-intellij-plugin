package com.github.mars05.crud.intellij.plugin.dao.model;

import com.github.mars05.crud.intellij.plugin.dto.FileTemplateDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectTemplateDO extends BaseDO {
    private String name;
    private String description;
    private Integer projectType;
    private String fileTemplates;

    private Long organizationId;
    private String organizationName;
    private String accessToken;
    private Integer publicFlag;

    private String createTime;
    private String updateTime;
    private Long createId;
    private String createName;
    private Long updateId;
    private String updateName;
}

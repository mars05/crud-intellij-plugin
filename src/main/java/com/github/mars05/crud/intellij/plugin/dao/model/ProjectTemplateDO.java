package com.github.mars05.crud.intellij.plugin.dao.model;

import lombok.Data;

/**
 * ProjectTemplateDO
 *
 * @author xiaoyu
 */
@Data
public class ProjectTemplateDO {
    private Long id;
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

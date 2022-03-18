package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
public class ProjectTemplateDTO {
    private Long id;
    private String name;
    private String description;
    private Integer projectType;
    public List<FileTemplateDTO> fileTemplateList;
    private String createTime;
    private String updateTime;
    private Long createId;
    private String createName;
    private Long updateId;
    private String updateName;
    private Long organizationId;
    private String organizationName;
    private String accessToken;
    private Integer publicFlag;
}

package com.github.mars05.crud.intellij.plugin.model.template;

import lombok.Data;

import java.util.List;

/**
 * 项目模板
 *
 * @author xiaoyu
 */
@Data
public class ProjectTemplate {

    /**
     * 名称
     */
    private String name;

    /**
     * 文件模板列表
     */
    private List<FileTemplate> fileTemplateList;

}

package com.github.mars05.crud.intellij.plugin.service;

import com.github.mars05.crud.intellij.plugin.model.template.ProjectTemplate;

import java.util.List;

/**
 * ProjectTemplateService
 *
 * @author xiaoyu
 */
public interface ProjectTemplateService {

    /**
     * 查询所有项目模板名称
     *
     * @return 项目模板名称列表
     */
    List<String> findAllName();

    /**
     * 通过名称获取项目模板
     *
     * @param name 项目模板名称
     * @return 项目模板
     */
    ProjectTemplate getByName(String name);

}

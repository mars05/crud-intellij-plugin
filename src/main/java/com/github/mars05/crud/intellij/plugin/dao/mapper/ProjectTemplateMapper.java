package com.github.mars05.crud.intellij.plugin.dao.mapper;

import com.github.mars05.crud.hub.common.entity.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class ProjectTemplateMapper extends AbstractMapper<ProjectTemplateDO> {
    @NotNull
    @Override
    protected List<ProjectTemplateDO> getDataList() {
        return CrudSettings.getProjectTemplates();
    }

    @NotNull
    @Override
    protected Class<ProjectTemplateDO> getDataClass() {
        return ProjectTemplateDO.class;
    }

    @Override
    protected Serializable getId(ProjectTemplateDO projectTemplateDO) {
        return projectTemplateDO.getId();
    }

}

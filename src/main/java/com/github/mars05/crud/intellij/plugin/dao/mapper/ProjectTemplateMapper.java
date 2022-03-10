package com.github.mars05.crud.intellij.plugin.dao.mapper;

import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectTemplateMapper extends AbstractMapper<ProjectTemplateDO> {

    @NotNull
    @Override
    protected List<ProjectTemplateDO> getDataList() {
        return CrudSettings.getInstance().getProjectTemplates();
    }

    @NotNull
    @Override
    protected Class<ProjectTemplateDO> getDataClass() {
        return ProjectTemplateDO.class;
    }


}

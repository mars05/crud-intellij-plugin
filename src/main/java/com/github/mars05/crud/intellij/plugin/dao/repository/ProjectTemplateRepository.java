package com.github.mars05.crud.intellij.plugin.dao.repository;

import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ProjectTemplateRepository extends AbstractRepository<ProjectTemplateDO> {

    private final List<ProjectTemplateDO> dataList = Objects.requireNonNull(CrudSettings.getInstance().getState()).getProjectTemplates();

    @NotNull
    @Override
    protected List<ProjectTemplateDO> getDataList() {
        return dataList;
    }

    @NotNull
    @Override
    protected Class<ProjectTemplateDO> getDataClass() {
        return ProjectTemplateDO.class;
    }

}

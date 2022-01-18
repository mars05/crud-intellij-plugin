package com.github.mars05.crud.intellij.plugin.dao.repository;

import com.github.mars05.crud.intellij.plugin.dao.model.FileTemplateDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileTemplateRepository extends AbstractRepository<FileTemplateDO> {
    private final List<FileTemplateDO> dataList = Objects.requireNonNull(CrudSettings.getInstance().getState()).getFileTemplates();

    @NotNull
    @Override
    protected List<FileTemplateDO> getDataList() {
        return dataList;
    }

    @NotNull
    @Override
    protected Class<FileTemplateDO> getDataClass() {
        return FileTemplateDO.class;
    }

    public List<FileTemplateDO> list(String projectTemplateId) {
        return list().stream().filter(data -> data.getProjectTemplateId().equals(projectTemplateId)).collect(Collectors.toList());
    }

}

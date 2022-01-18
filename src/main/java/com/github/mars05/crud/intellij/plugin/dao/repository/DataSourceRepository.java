package com.github.mars05.crud.intellij.plugin.dao.repository;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * 数据源
 */
@Data
public class DataSourceRepository extends AbstractRepository<DataSourceDO> {
    private final List<DataSourceDO> dataList = Objects.requireNonNull(CrudSettings.getInstance().getState()).getDataSources();

    @NotNull
    @Override
    protected List<DataSourceDO> getDataList() {
        return dataList;
    }

    @NotNull
    @Override
    protected Class<DataSourceDO> getDataClass() {
        return DataSourceDO.class;
    }

}
package com.github.mars05.crud.intellij.plugin.dao.mapper;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * 数据源
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DataSourceMapper extends AbstractMapper<DataSourceDO> {

    @NotNull
    @Override
    protected List<DataSourceDO> getDataList() {
        return CrudSettings.getDataSources();
    }

    @NotNull
    @Override
    protected Class<DataSourceDO> getDataClass() {
        return DataSourceDO.class;
    }

    @Override
    protected Serializable getId(DataSourceDO dataSourceDO) {
        return dataSourceDO.getId();
    }

}
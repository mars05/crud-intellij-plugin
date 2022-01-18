package com.github.mars05.crud.intellij.plugin.service;

import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Table;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.repository.DataSourceRepository;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceCreateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceRespDTO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceUpdateReqDTO;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.DataSourceVO;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.JdbcUtils;
import com.github.mars05.crud.intellij.plugin.util.Permit;
import com.github.mars05.crud.intellij.plugin.util.ValidateUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
@Slf4j
public class DataSourceService {
    private final DataSourceRepository dataSourceRepository = new DataSourceRepository();

    public List<DataSourceRespDTO> list() {
        return BeanUtils.convertList(dataSourceRepository.list(), DataSourceRespDTO.class);
    }

    public void create(DataSourceCreateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        if (dataSourceRepository.list().stream().anyMatch(dataSourceDO -> dataSourceDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        DataSourceDO dataSourceDO = BeanUtils.convertBean(reqDTO, DataSourceDO.class);
        dataSourceDO.setId(IdWorker.get32UUID());
        dataSourceRepository.create(dataSourceDO);
    }

    public void update(DataSourceUpdateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        DataSourceDO oldDataSourceDO = dataSourceRepository.detail(reqDTO.getId());
        Preconditions.checkNotNull(oldDataSourceDO, "数据源不存在");
        if (dataSourceRepository.list().stream().anyMatch(dataSourceDO -> !dataSourceDO.getId().equals(reqDTO.getId()) && dataSourceDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        DataSourceDO dataSourceDO = BeanUtils.convertBean(reqDTO, DataSourceDO.class);
        dataSourceRepository.update(dataSourceDO);
    }

    public DataSourceRespDTO detail(String id) {
        return BeanUtils.convertBean(dataSourceRepository.detail(id), DataSourceRespDTO.class);
    }

    public void delete(String id) {
        DataSourceDO oldDataSourceDO = dataSourceRepository.detail(id);
        Preconditions.checkNotNull(oldDataSourceDO, "数据源不存在");
        dataSourceRepository.delete(id);
    }

    public void testConnection(@NotNull DataSourceCreateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        String testSql;
        switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(reqDTO.getDatabaseType()))) {
            case MYSQL:
            case PG_SQL:
                testSql = "SELECT 1";
                break;
            case ORACLE:
                testSql = "SELECT 1 FROM DUAL";
                break;
            case SQL_SERVER:
            default:
                throw new UnsupportedOperationException("暂不支持数据库[" + reqDTO.getDatabaseType() + "]");
        }
        Connection connection = JdbcUtils.getConnection(BeanUtils.convertBean(reqDTO, DataSourceVO.class));
        try {
            connection.createStatement().execute(testSql);
        } catch (SQLException e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public List<String> allCatalog(String dsId) {
        DataSourceDO dataSourceDO = dataSourceRepository.detail(dsId);
        return JdbcUtils.getAllCatalog(BeanUtils.convertBean(dataSourceDO, DataSourceVO.class));
    }

    public List<String> allTableName(String dsId, String catalog) {
        DataSourceDO dataSourceDO = dataSourceRepository.detail(dsId);
        List<? extends Table> allTable = JdbcUtils.getAllTable(BeanUtils.convertBean(dataSourceDO, DataSourceVO.class), catalog);
        return allTable.stream().map(Table::getTableName).collect(Collectors.toList());
    }

    public com.github.mars05.crud.intellij.plugin.model.param.Table getTable(String dsId, String catalog, String tableName) {
        DataSourceDO dataSourceDO = dataSourceRepository.detail(dsId);
        List<? extends Table> allTable = JdbcUtils.getAllTable(BeanUtils.convertBean(dataSourceDO, DataSourceVO.class), catalog);
        return allTable.stream().filter(table -> table.getTableName().equals(tableName)).map(table -> {
            com.github.mars05.crud.intellij.plugin.model.param.Table t = new com.github.mars05.crud.intellij.plugin.model.param.Table();
            t.setTableName(table.getTableName());
            t.setRemarks(table.getRemarks());
            t.setColumns(allColumn(dsId, catalog, tableName));
            return t;
        }).findFirst().orElse(null);
    }

    public List<com.github.mars05.crud.intellij.plugin.model.param.Column> allColumn(String dsId, String catalog, String table) {
        DataSourceDO dataSourceDO = dataSourceRepository.detail(dsId);
        List<? extends Column> allColumn = JdbcUtils.getAllColumn(BeanUtils.convertBean(dataSourceDO, DataSourceVO.class), catalog, table);
        return allColumn.stream().map(column -> {
            com.github.mars05.crud.intellij.plugin.model.param.Column c = new com.github.mars05.crud.intellij.plugin.model.param.Column();
            c.setColumnName(column.getColumnName());
            c.setRemarks(column.getRemarks());
            c.setPrimaryKey(Boolean.valueOf(column.getPrimaryKey()));
            try {
                c.setType(Integer.parseInt(String.valueOf(Permit.getField(column.getClass(), "dataType").get(column))));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return c;
        }).collect(Collectors.toList());
    }

    public List<String> allColumnName(String dsId, String catalog, String table) {
        DataSourceDO dataSourceDO = dataSourceRepository.detail(dsId);
        List<? extends Column> allColumn = JdbcUtils.getAllColumn(BeanUtils.convertBean(dataSourceDO, DataSourceVO.class), catalog, table);
        return allColumn.stream().map(Column::getColumnName).collect(Collectors.toList());
    }

}

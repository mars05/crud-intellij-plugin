package com.github.mars05.crud.intellij.plugin.service;

import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.Table;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.mars05.crud.intellij.plugin.dao.mapper.DataSourceMapper;
import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceCreateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceDTO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceRespDTO;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceUpdateReqDTO;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.JdbcUtils;
import com.github.mars05.crud.intellij.plugin.util.Permit;
import com.github.mars05.crud.intellij.plugin.util.ValidateUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

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
    private final DataSourceMapper dataSourceRepository = new DataSourceMapper();

    public List<DataSourceRespDTO> list() {
        return BeanUtils.convertList(dataSourceRepository.selectList(), DataSourceRespDTO.class);
    }

    public void create(DataSourceCreateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        if (dataSourceRepository.selectList().stream().anyMatch(dataSourceDO -> dataSourceDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        DataSourceDO dataSourceDO = BeanUtils.convertBean(reqDTO, DataSourceDO.class);
        dataSourceDO.setId(IdWorker.getId());
        dataSourceRepository.insert(dataSourceDO);
    }

    public void update(DataSourceUpdateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        DataSourceDO oldDataSourceDO = dataSourceRepository.selectById(reqDTO.getId());
        Preconditions.checkNotNull(oldDataSourceDO, "数据源不存在");
        if (dataSourceRepository.selectList().stream().anyMatch(dataSourceDO -> !dataSourceDO.getId().equals(reqDTO.getId()) && dataSourceDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        DataSourceDO dataSourceDO = BeanUtils.convertBean(reqDTO, DataSourceDO.class);
        dataSourceRepository.updateById(dataSourceDO);
    }

    public DataSourceRespDTO detail(Long dsId) {
        return BeanUtils.convertBean(dataSourceRepository.selectById(dsId), DataSourceRespDTO.class);
    }

    public void delete(Long id) {
        DataSourceDO oldDataSourceDO = dataSourceRepository.selectById(id);
        Preconditions.checkNotNull(oldDataSourceDO, "数据源不存在");
        dataSourceRepository.deleteById(id);
    }

    public void testConnection(DataSourceCreateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        DataSourceDTO dataSourceDTO = BeanUtils.convertBean(reqDTO, DataSourceDTO.class);
        String testSql;
        switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(reqDTO.getDatabaseType()))) {
            case MYSQL:
            case PG_SQL:
                testSql = "SELECT 1";
                dataSourceDTO.setDatabase(reqDTO.getInitDb());
                break;
            case ORACLE:
                testSql = "SELECT 1 FROM DUAL";
                break;
            case SQL_SERVER:
            default:
                throw new UnsupportedOperationException("暂不支持数据库[" + reqDTO.getDatabaseType() + "]");
        }
        Connection connection = JdbcUtils.getConnection(dataSourceDTO);
        try {
            connection.createStatement().execute(testSql);
        } catch (SQLException e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public List<String> allDatabase(Long dsId) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        DataSourceDTO dataSourceDTO = BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class);
        if (dataSourceDO.getDatabaseType().equals(DatabaseTypeEnum.PG_SQL.getCode())) {
            dataSourceDTO.setDatabase(dataSourceDO.getInitDb());
        }
        return JdbcUtils.getAllCatalog(dataSourceDTO);
    }

    public List<String> allSchema(Long dsId, String database) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        DataSourceDTO dataSourceDTO = BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class);
        dataSourceDTO.setDatabase(database);
        return JdbcUtils.getAllSchema(dataSourceDTO);
    }

    public List<String> allTableName(Long dsId, String database) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        List<? extends Table> allTable = JdbcUtils.getAllTable(BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class), database);
        return allTable.stream().map(Table::getTableName).collect(Collectors.toList());
    }

    public List<String> allTableName(Long dsId, String database, String schema) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        DataSourceDTO dataSourceDTO = BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class);
        dataSourceDTO.setDatabase(database);
        List<? extends Table> allTable = JdbcUtils.getAllTable(dataSourceDTO, database, schema);
        return allTable.stream().map(Table::getTableName).collect(Collectors.toList());
    }

    public com.github.mars05.crud.intellij.plugin.model.Table getTable(Long dsId, String database, String tableName) {
        return this.getTable(dsId, database, null, tableName);
    }

    public com.github.mars05.crud.intellij.plugin.model.Table getTable(Long dsId, String database, String schema, String tableName) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        List<? extends Table> allTable = JdbcUtils.getAllTable(BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class), database, schema);
        return allTable.stream().filter(table -> table.getTableName().equals(tableName)).map(table -> {
            com.github.mars05.crud.intellij.plugin.model.Table t = new com.github.mars05.crud.intellij.plugin.model.Table();
            t.setTableName(table.getTableName());
            t.setRemarks(table.getRemarks());
            t.setColumns(allColumn(dsId, database, schema, tableName));
            return t;
        }).findFirst().orElse(null);
    }

    public List<com.github.mars05.crud.intellij.plugin.model.Column> allColumn(Long dsId, String database, String schema, String table) {
        DataSourceDO dataSourceDO = dataSourceRepository.selectById(dsId);
        List<? extends Column> allColumn = JdbcUtils.getAllColumn(BeanUtils.convertBean(dataSourceDO, DataSourceDTO.class), database, schema, table);
        return allColumn.stream().map(column -> {
            com.github.mars05.crud.intellij.plugin.model.Column c = new com.github.mars05.crud.intellij.plugin.model.Column();
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

}

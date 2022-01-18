package com.github.mars05.crud.intellij.plugin.util;

import cn.smallbun.screw.core.metadata.Column;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.metadata.Table;
import cn.smallbun.screw.core.query.mysql.model.MySqlColumnModel;
import cn.smallbun.screw.core.query.oracle.model.OracleColumnModel;
import cn.smallbun.screw.core.query.postgresql.model.PostgreSqlColumnModel;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.DataSourceVO;
import com.github.mars05.crud.intellij.plugin.util.jdbc.AbstractDatabaseQuery;
import com.github.mars05.crud.intellij.plugin.util.jdbc.MySqlDataBaseQuery;
import com.github.mars05.crud.intellij.plugin.util.jdbc.OracleDataBaseQuery;
import com.github.mars05.crud.intellij.plugin.util.jdbc.PostgreSqlDataBaseQuery;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
public class JdbcUtils {

    @NotNull
    public static Connection getConnection(DataSourceVO dataSourceVO) {
        try {
            String url = null;
            String driverClassName = null;
            switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(dataSourceVO.getDatabaseType()))) {
                case MYSQL:
                    url = "jdbc:mysql://";
                    driverClassName = "com.mysql.jdbc.Driver";
                    break;
                case PG_SQL:
                    url = "jdbc:postgresql://";
                    driverClassName = "org.postgresql.Driver";
                    break;
                case ORACLE:
                    url = "jdbc:oracle:thin:@//";
                    driverClassName = "oracle.jdbc.OracleDriver";
                    break;
                case SQL_SERVER:
                    throw new UnsupportedOperationException("暂不支持数据库[" + dataSourceVO.getDatabaseType() + "]");
            }
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            url = url + dataSourceVO.getHost() + ":" + dataSourceVO.getPort();
            if (StringUtils.isNotBlank(dataSourceVO.getDatabase())) {
                url = url + "/" + dataSourceVO.getDatabase();
            }
            List<String> optionList = new ArrayList<>();
//            if (DatabaseTypeEnum.PG_SQL.getCode().equals(dataSourceVO.getDatabaseType())
//                    && StringUtils.isNotBlank(dataSourceVO.getSchema())) {
//                optionList.add("currentSchema=" + dataSourceVO.getSchema());
//            }
            if (StringUtils.isNotBlank(dataSourceVO.getOption())) {
                optionList.add(dataSourceVO.getOption());
            }
            if (!optionList.isEmpty()) {
                url = url + "?" + String.join("&", optionList);
            }
            Properties props = new Properties();
            props.put("user", dataSourceVO.getUsername());
            props.put("password", dataSourceVO.getPassword());
            props.put("remarks", "true");
            props.put("useInformationSchema", "true");
            props.put("ResultSetMetaDataOptions", "1");
            return DriverManager.getConnection(url, props);
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public static List<String> getAllCatalog(DataSourceVO dataSourceVO) {
        AbstractDatabaseQuery baseQuery = null;
        try {
            switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(dataSourceVO.getDatabaseType()))) {
                case MYSQL:
                    baseQuery = new MySqlDataBaseQuery(dataSourceVO);
                    return baseQuery.getCatalogs();
                case ORACLE:
                    baseQuery = new OracleDataBaseQuery(dataSourceVO);
                    return baseQuery.getCatalogs();
                case PG_SQL:
                    baseQuery = new PostgreSqlDataBaseQuery(dataSourceVO);
                    return baseQuery.getCatalogs();
                case SQL_SERVER:
                default:
                    throw new BizException("暂不支持");
            }
        } finally {
            if (baseQuery != null) {
                try {
                    ((Connection) Permit.getField(baseQuery.getClass(), "connection").get(baseQuery)).close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static List<? extends Table> getAllTable(DataSourceVO dataSourceVO, String catalog) {
        AbstractDatabaseQuery baseQuery = null;
        try {
            switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(dataSourceVO.getDatabaseType()))) {
                case MYSQL:
                    baseQuery = new MySqlDataBaseQuery(dataSourceVO, catalog);
                    return baseQuery.getTables();
                case ORACLE:
                    baseQuery = new OracleDataBaseQuery(dataSourceVO, catalog);
                    return baseQuery.getTables();
                case PG_SQL:
                    baseQuery = new PostgreSqlDataBaseQuery(dataSourceVO, catalog);
                    return baseQuery.getTables();
                case SQL_SERVER:
                default:
                    throw new BizException("暂不支持");
            }
        } finally {
            if (baseQuery != null) {
                try {
                    ((Connection) Permit.getField(baseQuery.getClass(), "connection").get(baseQuery)).close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static List<? extends Column> getAllColumn(DataSourceVO dataSourceVO, String catalog, String tableName) {
        AbstractDatabaseQuery baseQuery = null;
        List<? extends PrimaryKey> primaryKeys;
        try {
            switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(dataSourceVO.getDatabaseType()))) {
                case MYSQL:
                    baseQuery = new MySqlDataBaseQuery(dataSourceVO, catalog, tableName);
                    primaryKeys = baseQuery.getPrimaryKeys(tableName);
                    return baseQuery.getTableColumns(tableName).stream().peek(column -> {
                        ((MySqlColumnModel) column).setPrimaryKey(String.valueOf(primaryKeys.stream().anyMatch(pk -> pk.getColumnName().equals(column.getColumnName()))));
                    }).collect(Collectors.toList());
                case ORACLE:
                    baseQuery = new OracleDataBaseQuery(dataSourceVO, catalog, tableName);
                    primaryKeys = baseQuery.getPrimaryKeys(tableName);
                    return baseQuery.getTableColumns(tableName).stream().peek(column -> {
                        ((OracleColumnModel) column).setPrimaryKey(String.valueOf(primaryKeys.stream().anyMatch(pk -> pk.getColumnName().equals(column.getColumnName()))));
                    }).collect(Collectors.toList());
                case PG_SQL:
                    baseQuery = new PostgreSqlDataBaseQuery(dataSourceVO, catalog, tableName);
                    primaryKeys = baseQuery.getPrimaryKeys(tableName);
                    return baseQuery.getTableColumns(tableName).stream().peek(column -> {
                        ((PostgreSqlColumnModel) column).setPrimaryKey(String.valueOf(primaryKeys.stream().anyMatch(pk -> pk.getColumnName().equals(column.getColumnName()))));
                    }).collect(Collectors.toList());
                case SQL_SERVER:
                default:
                    throw new BizException("暂不支持");
            }
        } finally {
            if (baseQuery != null) {
                try {
                    ((Connection) Permit.getField(baseQuery.getClass(), "connection").get(baseQuery)).close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static List<String> getSelectColumn(DataSourceVO dataSourceVO, String querySql) {
        AbstractDatabaseQuery baseQuery = null;
        try {
            switch (Objects.requireNonNull(DatabaseTypeEnum.findByCode(dataSourceVO.getDatabaseType()))) {
                case MYSQL:
                    baseQuery = new MySqlDataBaseQuery(dataSourceVO);
                    return baseQuery.getColumnsByQuerySql(querySql);
                case ORACLE:
                    baseQuery = new OracleDataBaseQuery(dataSourceVO);
                    return baseQuery.getColumnsByQuerySql(querySql);
                case PG_SQL:
                    baseQuery = new PostgreSqlDataBaseQuery(dataSourceVO);
                    return baseQuery.getColumnsByQuerySql(querySql);
                case SQL_SERVER:
                default:
                    throw new BizException("暂不支持");
            }
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        } finally {
            if (baseQuery != null) {
                try {
                    ((Connection) Permit.getField(baseQuery.getClass(), "connection").get(baseQuery)).close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}

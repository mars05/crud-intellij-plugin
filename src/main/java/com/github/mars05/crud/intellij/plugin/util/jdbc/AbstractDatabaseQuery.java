/*
 * screw-core - 简洁好用的数据库表结构文档生成工具
 * Copyright © 2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.mars05.crud.intellij.plugin.util.jdbc;

import cn.smallbun.screw.core.exception.QueryException;
import cn.smallbun.screw.core.metadata.PrimaryKey;
import cn.smallbun.screw.core.query.DatabaseQuery;
import cn.smallbun.screw.core.util.Assert;
import cn.smallbun.screw.core.util.ExceptionUtils;
import cn.smallbun.screw.core.util.StringUtils;
import com.github.mars05.crud.intellij.plugin.dto.DataSourceDTO;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.smallbun.screw.core.constant.DefaultConstants.NOT_SUPPORTED;

/**
 * AbstractDataBaseQuery
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 12:50
 */
public abstract class AbstractDatabaseQuery implements DatabaseQuery {
    /**
     * DataSource
     */
    @Getter
    private final DataSourceDTO dataSource;
    private String catalog;
    private String schema;

    /**
     * Connection 双重锁，线程安全
     */
    volatile protected Connection connection;

    public AbstractDatabaseQuery(DataSourceDTO dataSource) {
        this.dataSource = dataSource;
    }

    public AbstractDatabaseQuery(DataSourceDTO dataSource, String catalog) {
        this.dataSource = dataSource;
        this.catalog = catalog;
    }

    public AbstractDatabaseQuery(DataSourceDTO dataSource, String catalog, String schema) {
        this.dataSource = dataSource;
        this.catalog = catalog;
        this.schema = schema;
    }

    /**
     * 获取连接对象，单例模式，采用双重锁检查
     *
     * @return {@link Connection}
     * @throws QueryException QueryException
     */
    protected Connection getConnection() throws QueryException {
        try {
            //不为空
            if (!Objects.isNull(connection) && !connection.isClosed()) {
                return connection;
            }
            //同步代码块
            synchronized (AbstractDatabaseQuery.class) {
                //为空或者已经关闭
                if (Objects.isNull(connection) || connection.isClosed()) {
                    this.connection = com.github.mars05.crud.intellij.plugin.util.JdbcUtils.getConnection(dataSource);
                }
            }
            return this.connection;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    public List<String> getCatalogs() throws QueryException {
        try {
            ResultSet catalogs = this.getConnection().getMetaData().getCatalogs();
            List<String> rs = new ArrayList<>();
            while (catalogs.next()) {
                rs.add(catalogs.getString("TABLE_CAT"));
            }
            return rs;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 获取 getCatalog
     *
     * @return {@link String}
     * @throws QueryException QueryException
     */
    protected String getCatalog() throws QueryException {
        if (this.catalog != null) {
            return this.catalog;
        }
        try {
            String catalog = this.getConnection().getCatalog();
            if (StringUtils.isBlank(catalog)) {
                return null;
            }
            return catalog;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 获取 getSchema
     *
     * @return {@link String}
     * @throws QueryException QueryException
     */
    protected String getSchema() throws QueryException {
        if (this.schema != null) {
            return this.schema;
        }
        try {
            String schema = null;
            //获取数据库URL 用于判断数据库类型
//            DruidDataSource hikariDataSource = (DruidDataSource) this.getDataSource();
//            String url = hikariDataSource.getRawJdbcUrl();
//            //获取数据库名称
//            String name = JdbcUtils.getDbType(url).getName();
//            if (DatabaseType.CACHEDB.getName().equals(name)) {
//                schema = verifySchema(hikariDataSource);
//            } else {
            schema = this.getConnection().getSchema();
//            }

            if (StringUtils.isBlank(schema)) {
                return null;
            }
            return schema;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 验证Schema
     *
     * @param hikariDataSource hikariDataSource
     * @return Schema
     */
//    private String verifySchema(HikariDataSource hikariDataSource) throws SQLException {
//        String schema = hikariDataSource.getSchema();
//        //验证是否有此Schema
//        ResultSet resultSet = this.getConnection().getMetaData().getSchemas();
//        while (resultSet.next()) {
//            int columnCount = resultSet.getMetaData().getColumnCount();
//            for (int i = 1; i <= columnCount; i++) {
//                String columnValue = resultSet.getString(i);
//                if (StringUtils.isNotBlank(columnValue) && columnValue.contains(schema)) {
//                    return schema;
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 获取 DatabaseMetaData
     *
     * @return {@link DatabaseMetaData}
     * @throws QueryException QueryException
     */
    protected DatabaseMetaData getMetaData() throws QueryException {
        try {
            return this.getConnection().getMetaData();
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 准备声明
     *
     * @param sql {@link String} SQL
     * @return {@link PreparedStatement}
     * @throws QueryException QueryException
     */
    protected PreparedStatement prepareStatement(String sql) throws QueryException {
        Assert.notEmpty(sql, "Sql can not be empty!");
        try {
            return this.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 根据表名获取主键
     *
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

    public List<String> getColumnsByQuerySql(String querySql) throws QueryException {
        try {
            List<String> res = Lists.newArrayList();
            //替换sql中的from 、where、on、group、order为小写
            String sql;
            querySql = querySql.replace(";", "");
            querySql = querySql.replace("\nFROM ", " from ").replace("\nfrom ", " from ").replace(" FROM ", " from ");
            querySql = querySql.replace("\nWHERE ", " where ").replace("\nwhere ", " where ").replace(" WHERE ", " where ");
            querySql = querySql.replace("\nON ", " on ").replace("\non ", " on ").replace(" ON ", " on ");
            querySql = querySql.replace("\nGROUP ", " group ").replace("\ngroup ", " group ").replace(" GROUP ", " group ");
            querySql = querySql.replace("\nORDER ", " order ").replace("\norder ", " order ").replace(" ORDER ", " order ");
            //寻找from 、where、on、group、order在sql中的位置
            int idx_from = querySql.lastIndexOf(" from ");
            int idx_where = querySql.lastIndexOf(" where ");
            int idx_on = querySql.lastIndexOf(" on ");
            int idx_group = querySql.lastIndexOf(" group ");
            int idx_order = querySql.lastIndexOf(" order ");
            //根据各情况添加where 1=0 或on 1=0
            if ((idx_where > idx_on && idx_on > idx_from) || (idx_where > idx_from && idx_from > idx_on)) {
                sql = replaceLast(querySql, " where ", " where 1=0 and ");
            } else if (idx_group > idx_on && idx_on > idx_from) {
                sql = replaceLast(querySql, " group ", " where 1=0 group ");
            } else if (idx_order > idx_on && idx_on > idx_from) {
                sql = replaceLast(querySql, " order ", " where 1=0 order ");
            } else {
                sql = querySql.concat(" where 1=0");
            }
            //获取所有字段
            try (Statement stmt = this.getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    res.add(metaData.getColumnLabel(i));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }
    }

    public static String replaceLast(final String text, final String strToReplace, final String replaceWithThis) {
        return text.replaceFirst("(?s)" + strToReplace + "(?!.*?" + strToReplace + ")", replaceWithThis);
    }
}

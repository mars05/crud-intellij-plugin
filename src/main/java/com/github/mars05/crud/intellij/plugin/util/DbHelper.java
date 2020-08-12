package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.model.Column;
import com.github.mars05.crud.intellij.plugin.model.Table;
import com.mysql.jdbc.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author xiaoyu
 */
public class DbHelper {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String db;
    private Properties props;

    public DbHelper(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        props = new Properties();
        props.put("user", this.username);
        props.put("password", this.password);
        props.setProperty("remarks", "true");
        props.put("useInformationSchema", "true");
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", props);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Connection getConnection(String database) {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", props);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public List<String> getDatabases() {
        Connection conn = getConnection();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            List<String> rs = new ArrayList<>();
            while (catalogs.next()) {
                String db = catalogs.getString("TABLE_CAT");
                if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(db, "information_schema")) {
                    continue;
                }
                rs.add(db);
            }
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public List<String> getAllTableName(String database) {
        db = database;
        Connection conn = getConnection(db);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            List<String> ls = new ArrayList<>();
            while (rs.next()) {
                String s = rs.getString("TABLE_NAME");
                ls.add(s);
            }
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public Table getTable(String tableName) {
        Connection conn = getConnection(db);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, "", tableName, new String[]{"TABLE"});
            Table table = null;
            if (rs.next()) {
                table = new Table(rs.getString("REMARKS"), tableName, getAllColumn(tableName, conn));
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private List<Column> getAllColumn(String tableName, Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            String primaryKey = null;
            while (primaryKeys.next()) {
                primaryKey = primaryKeys.getString("COLUMN_NAME");
            }
            ResultSet rs = metaData.getColumns(null, null, tableName, null);
            List<Column> ls = new ArrayList<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                Column column = new Column(rs.getString("REMARKS"), columnName, rs.getInt("DATA_TYPE"));
                if (!StringUtils.isNullOrEmpty(primaryKey) && columnName.equals(primaryKey)) {
                    column.setId(true);
                }
                ls.add(column);
            }
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

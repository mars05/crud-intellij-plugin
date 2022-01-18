package com.github.mars05.crud.intellij.plugin.util;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
public class SqlUtils {
    public static List<String> getSelectColumnList(String sql, DatabaseTypeEnum databaseTypeEnum) {
        SQLStatement sqlStatement = getSqlStatement(sql, databaseTypeEnum);
        if (!(sqlStatement instanceof SQLSelectStatement)) {
            throw new BizException("SQL错误，只能输入查询SQL语句");
        }
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlSelectStatement.getSelect().getQuery();
        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        return selectList.stream().map(sqlSelectItem -> sqlSelectItem.getAlias() != null ? sqlSelectItem.getAlias() : StringUtils.remove(sqlSelectItem.toString(), "`"))
                .collect(Collectors.toList());
    }

    public static Table getTableByColumn(String sql, String ColumnAlias, DatabaseTypeEnum databaseTypeEnum) {
        SQLStatement sqlStatement = getSqlStatement(sql, databaseTypeEnum);
        if (!(sqlStatement instanceof SQLSelectStatement)) {
            throw new BizException("SQL错误，只能输入查询SQL语句");
        }
        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
        SQLSelectQuery query = sqlSelectStatement.getSelect().getQuery();
        if (query instanceof SQLUnionQuery) {
            query = ((SQLUnionQuery) query).getChildren().get(0);
//            throw new BizException("无法分析SQL");
        }
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
        List<SQLSelectItem> selectList = queryBlock.getSelectList();

        Table table = new Table();
        table.setColumnList(new ArrayList<>());

        List<Column> columnList = selectList.stream().map(sqlSelectItem -> {
            Column column = new Column();
            column.setColumnAlias(sqlSelectItem.getAlias() != null ? sqlSelectItem.getAlias() : StringUtils.remove(sqlSelectItem.toString(), "`"));
            SQLExpr expr = sqlSelectItem.getExpr();
            if (expr instanceof SQLMethodInvokeExpr) {
                column.setColumnName(column.getColumnAlias());
                return column;
            }
            try {
                Object name = Permit.getField(expr.getClass(), "name").get(expr);
                column.setColumnName(String.valueOf(name));
            } catch (Exception e) {
                throw new BizException(e.getMessage(), e);
            }
            return column;
        }).collect(Collectors.toList());

        Column traceColumn = columnList.stream().filter(column1 -> column1.getColumnAlias().equalsIgnoreCase(ColumnAlias)).findFirst().orElse(null);
        if (traceColumn == null) {
            throw new BizException("溯源失败,溯源字段不在SQL中");
        }
        SchemaStatVisitor visitor = getVisitor(sql, databaseTypeEnum);
        TableStat.Column column2 = visitor.getColumns().stream().filter(column -> column.getName().equals(StringUtils.remove(traceColumn.getColumnName(), "\""))).findFirst().orElse(null);
        if (column2 == null) {
            throw new BizException("溯源失败,溯源字段不在源端表中");
        }
        if (column2.getTable().contains(".")) {
            table.setTableName(StringUtils.substringAfterLast(column2.getTable(), "."));
        } else {
            table.setTableName(column2.getTable());
        }
        List<TableStat.Column> tableColumnList = visitor.getColumns().stream().filter(column -> column.getTable().equals(column2.getTable())).collect(Collectors.toList());
        for (TableStat.Column c1 : tableColumnList) {
            for (Column c2 : columnList) {
                if (c1.getName().equals(StringUtils.remove(c2.getColumnName(), "\""))) {
                    table.getColumnList().add(c2);
                }
            }
        }
        return table;
    }

    public static SchemaStatVisitor getVisitor(String sql, DatabaseTypeEnum databaseTypeEnum) {
        SQLStatement sqlStatement = getSqlStatement(sql, databaseTypeEnum);
        SchemaStatVisitor visitor = null;
        switch (databaseTypeEnum) {
            case MYSQL: {
                visitor = new MySqlSchemaStatVisitor();
                break;
            }
            case PG_SQL:
                visitor = new PGSchemaStatVisitor();
                break;
            case ORACLE: {
                visitor = new OracleSchemaStatVisitor();
                break;
            }
            case SQL_SERVER: {
                visitor = new SQLServerSchemaStatVisitor();
                break;
            }
        }
        sqlStatement.accept(visitor);
        return visitor;
    }

    public static SQLStatement getSqlStatement(String sql, DatabaseTypeEnum databaseTypeEnum) {
        Preconditions.checkNotNull(databaseTypeEnum, "数据库类型错误");
        List<SQLStatement> sqlStatements = null;
        switch (databaseTypeEnum) {
            case MYSQL: {
                sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
                break;
            }
            case PG_SQL:
                sqlStatements = SQLUtils.parseStatements(sql, DbType.postgresql);
                break;
            case ORACLE: {
                sqlStatements = SQLUtils.parseStatements(sql, DbType.oracle);
                break;
            }
            case SQL_SERVER:
                throw new BizException("暂不支持的数据库类型");
        }
        if (sqlStatements == null || sqlStatements.size() != 1) {
            throw new BizException("SQL错误，请输入单条SQL语句");
        }
        return sqlStatements.get(0);
    }

    @Data
    public static class Table {
        private String tableName;
        private List<Column> columnList;
    }

    @Data
    @Accessors(chain = true)
    public static class Column {
        private String columnAlias;
        private String columnName;

        public void setColumnAlias(String columnAlias) {
            if (StringUtils.contains(columnAlias, ".")) {
                this.columnAlias = StringUtils.substringAfterLast(columnAlias, ".");
            } else {
                this.columnAlias = columnAlias;
            }
        }
    }

}

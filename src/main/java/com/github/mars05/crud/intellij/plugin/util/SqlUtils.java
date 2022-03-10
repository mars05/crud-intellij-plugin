package com.github.mars05.crud.intellij.plugin.util;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.param.Column;
import com.github.mars05.crud.intellij.plugin.model.param.Table;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoyu
 */
public class SqlUtils {

    public static List<Table> getTablesByDdl(String ddl, DatabaseTypeEnum databaseTypeEnum) {
        List<Table> tableList = new ArrayList<>();
        List<SQLStatement> sqlStatementList;
        try {
            sqlStatementList = getSqlStatement(ddl, databaseTypeEnum);
        } catch (Exception e) {
            throw new BizException("DDL解析失败");
        }
        for (SQLStatement sqlStatement : sqlStatementList) {
            Table table = new Table();
            SQLCreateTableStatement statement = (SQLCreateTableStatement) sqlStatement;
            table.setTableName(StringUtils.remove(statement.getTableName(), "`"));
            table.setRemarks(((SQLCharExpr) statement.getComment()).getText());
            table.setColumns(new ArrayList<>());
            for (SQLColumnDefinition column : statement.getColumnDefinitions()) {
                Column c = new Column();
                c.setColumnName(StringUtils.remove(column.getColumnName(), "`"));
                c.setRemarks(((SQLCharExpr) column.getComment()).getText());
                c.setPrimaryKey(column.isPrimaryKey());
                c.setType(column.getDataType().jdbcType());
                table.getColumns().add(c);
            }
            tableList.add(table);
        }
        return tableList;
    }

    private static SchemaStatVisitor getVisitor(SQLStatement sqlStatement, DatabaseTypeEnum databaseTypeEnum) {
        SchemaStatVisitor visitor;
        switch (databaseTypeEnum) {
            case MYSQL:
                visitor = new MySqlSchemaStatVisitor();
                break;
            case PG_SQL:
                visitor = new PGSchemaStatVisitor();
                break;
            case ORACLE:
                visitor = new OracleSchemaStatVisitor();
                break;
            case SQL_SERVER:
                visitor = new SQLServerSchemaStatVisitor();
                break;
            default:
                throw new BizException("暂不支持的数据库类型");
        }
        sqlStatement.accept(visitor);
        return visitor;
    }

    private static List<SQLStatement> getSqlStatement(String sql, DatabaseTypeEnum databaseTypeEnum) {
        List<SQLStatement> sqlStatements;
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
            default:
                throw new BizException("暂不支持的数据库类型");
        }
        return sqlStatements;
    }

}

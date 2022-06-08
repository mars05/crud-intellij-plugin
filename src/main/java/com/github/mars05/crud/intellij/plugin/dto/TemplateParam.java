package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.intellij.plugin.model.Column;
import com.github.mars05.crud.intellij.plugin.model.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaoyu
 */
@Data
@Accessors(chain = true)
public class TemplateParam {
    private String projectName;
    private String basePackage;
    private String basePackageDir;

    private Maven maven;
    private DataSource dataSource;

    private Table table;

    public void setTable(Table table) {
        this.table = table;
        if (this.table.getRemarks() == null) {
            this.table.setRemarks(this.table.getTableName());
        }
        for (Column column : this.table.getColumns()) {
            if (column.getRemarks() == null) {
                column.setRemarks(column.getColumnName());
            }
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Maven {
        private String groupId;
        private String artifactId;
        private String version;
    }

    @Data
    @Accessors(chain = true)
    public static class DataSource {
        private String databaseType;
        private String host;
        private Integer port;
        private String username;
        private String password;
        private String database;
        private String schema;
    }
}

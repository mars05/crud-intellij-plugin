package com.github.mars05.crud.intellij.plugin.model;


import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xiaoyu
 */
public class Table {
    @Setter
    @Getter
    private String tableName;
    private String lowerCamelName;
    private String upperCamelName;
    @Setter
    @Getter
    private String remarks;
    @Setter
    @Getter
    private List<Column> columns;

    public String getLowerCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
    }

    public String getUpperCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
    }

}
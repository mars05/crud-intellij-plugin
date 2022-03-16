package com.github.mars05.crud.intellij.plugin.model;


import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xiaoyu
 */
@Setter
@Getter
public class Table {
    private String tableName;
    private String lowerCamelName;
    private String upperCamelName;
    private String remarks;
    private List<Column> columns;

    public String getLowerCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
    }

    public String getUpperCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
    }

}
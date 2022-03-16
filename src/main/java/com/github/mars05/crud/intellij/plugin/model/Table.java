package com.github.mars05.crud.intellij.plugin.model;


import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<String> imports;

    public String getLowerCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
    }

    public String getUpperCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
    }

    public Set<String> getImports() {
        imports = new HashSet<>();
        for (Column column : columns) {
            if (!column.getJavaTypeClass().isPrimitive() && !"java.lang".equals(StringUtils.substringBeforeLast(column.getJavaTypeClass().getName(), "."))) {
                imports.add(column.getJavaTypeClass().getName());
            }
        }
        return imports;
    }
}
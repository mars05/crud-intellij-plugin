package com.github.mars05.crud.intellij.plugin.model;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @author xiaoyu
 */
public class Field {
    private String comment;
    private String columnName;
    private Class<?> type;
    private boolean id;

    /**
     * @param comment    字段注释
     * @param type       字段类型
     * @param columnName 列的名称
     */
    public Field(String comment, Class<?> type, String columnName) {
        this.comment = comment;
        this.type = type;
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }


    public String getTypeName() {
        return type.getName();
    }

    public String getTypeSimpleName() {
        return type.getSimpleName();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isId() {
        return id;
    }

    public boolean isImport() {
        String typeName = getTypeName();
        try {
            if ("java.lang".equals(StringUtils.substringBeforeLast(typeName, ".")) || Class.forName(typeName).isPrimitive()) {
                return false;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}

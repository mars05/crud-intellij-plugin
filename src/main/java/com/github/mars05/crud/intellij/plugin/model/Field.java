package com.github.mars05.crud.intellij.plugin.model;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

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
//        if (StringUtils.startsWith(columnName, "is_")) {
//            type = boolean.class;
//        }
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
        String str = columnName;
//        if (StringUtils.startsWith(columnName, "is_")) {
//            str = StringUtils.substringAfter(columnName, "is_");
//        }
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isId() {
        return id;
    }

    public boolean isImport() {
        String typeName = getTypeName();
        if (type.isPrimitive() || "java.lang".equals(StringUtils.substringBeforeLast(typeName, "."))) {
            return false;
        }
        return true;
    }
}

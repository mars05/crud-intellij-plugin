package com.github.mars05.crud.intellij.plugin.model;

import com.github.mars05.crud.intellij.plugin.util.JavaTypeUtils;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaoyu
 */
public class Column {
    @Setter
    @Getter
    private String columnName;
    private String lowerCamelName;
    private String upperCamelName;
    @Setter
    @Getter
    private String remarks;
    /**
     * 字段类型 ({@link java.sql.Types})
     *
     * @see java.sql.Types
     */
    @Setter
    @Getter
    private Integer type;
    /**
     * 字段类型对应java类型的class
     */
    private Class<?> javaTypeClass;
    /**
     * 字段类型对应java类型的名称
     */
    private String javaTypeName;
    @Setter
    @Getter
    private Boolean primaryKey;

    public String getLowerCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }

    public String getUpperCamelName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName);
    }

    public Class<?> getJavaTypeClass() {
        return JavaTypeUtils.convertType(type);
    }

    public String getJavaTypeName() {
        return JavaTypeUtils.convertType(type).getSimpleName();
    }

}

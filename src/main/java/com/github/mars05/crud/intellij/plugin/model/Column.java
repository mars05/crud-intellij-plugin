package com.github.mars05.crud.intellij.plugin.model;

import com.github.mars05.crud.intellij.plugin.util.JavaTypeUtils;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaoyu
 */
@Setter
@Getter
public class Column {
    private String columnName;
    private String lowerCamelName;
    private String upperCamelName;
    private String remarks;
    /**
     * 字段类型 ({@link java.sql.Types})
     *
     * @see java.sql.Types
     */
    private Integer type;
    /**
     * 字段类型对应java类型的class
     */
    private Class<?> javaTypeClass;
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


}

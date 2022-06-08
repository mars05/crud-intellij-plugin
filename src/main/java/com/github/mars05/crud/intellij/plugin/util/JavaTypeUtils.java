package com.github.mars05.crud.intellij.plugin.util;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author xiaoyu
 */
public class JavaTypeUtils {
    private static LinkedHashMap<JDBCType, Class<?>> TYPE_TO_CLASS = new LinkedHashMap();
    private static LinkedHashMap<Class<?>, JDBCType> CLASS_TO_TYPE = new LinkedHashMap();

    static {
        //字符串类型
        TYPE_TO_CLASS.put(JDBCType.VARCHAR, String.class);
        TYPE_TO_CLASS.put(JDBCType.LONGVARCHAR, String.class);
        TYPE_TO_CLASS.put(JDBCType.CHAR, String.class);
        //整数类型
        TYPE_TO_CLASS.put(JDBCType.NUMERIC, Integer.class);
        TYPE_TO_CLASS.put(JDBCType.INTEGER, Integer.class);
        TYPE_TO_CLASS.put(JDBCType.BIGINT, Long.class);
        TYPE_TO_CLASS.put(JDBCType.SMALLINT, Integer.class);
        TYPE_TO_CLASS.put(JDBCType.TINYINT, Integer.class);
        //浮点类型
        TYPE_TO_CLASS.put(JDBCType.FLOAT, Float.class);
        TYPE_TO_CLASS.put(JDBCType.DOUBLE, Double.class);
        TYPE_TO_CLASS.put(JDBCType.DECIMAL, BigDecimal.class);
        //其他类型
        TYPE_TO_CLASS.put(JDBCType.BOOLEAN, Boolean.class);
        TYPE_TO_CLASS.put(JDBCType.DATE, Date.class);
        TYPE_TO_CLASS.put(JDBCType.TIME, Date.class);
        TYPE_TO_CLASS.put(JDBCType.TIMESTAMP, Date.class);
        TYPE_TO_CLASS.put(JDBCType.BIT, boolean.class);

        CLASS_TO_TYPE.put(String.class, JDBCType.VARCHAR);
        CLASS_TO_TYPE.put(Short.class, JDBCType.INTEGER);
        CLASS_TO_TYPE.put(Integer.class, JDBCType.INTEGER);
        CLASS_TO_TYPE.put(Long.class, JDBCType.BIGINT);
        CLASS_TO_TYPE.put(Float.class, JDBCType.FLOAT);
        CLASS_TO_TYPE.put(Double.class, JDBCType.DOUBLE);
        CLASS_TO_TYPE.put(BigDecimal.class, JDBCType.DECIMAL);
        CLASS_TO_TYPE.put(Boolean.class, JDBCType.BOOLEAN);
        CLASS_TO_TYPE.put(Date.class, JDBCType.DATE);
        CLASS_TO_TYPE.put(LocalDateTime.class, JDBCType.DATE);
        CLASS_TO_TYPE.put(LocalDate.class, JDBCType.DATE);
        CLASS_TO_TYPE.put(LocalTime.class, JDBCType.DATE);
        CLASS_TO_TYPE.put(boolean.class, JDBCType.BIT);

    }

    /**
     * 数据库字段类型 转 Java类型
     *
     * @param sqlType
     * @see Types
     * @see JDBCType
     */
    public static Class<?> convertType(int sqlType) {
        return TYPE_TO_CLASS.getOrDefault(JDBCType.valueOf(sqlType), Object.class);
    }

    public static Integer ofType(Class<?> clazz) {
        JDBCType jdbcType = CLASS_TO_TYPE.get(clazz);
        if (jdbcType == null) {
            return null;
        }
        return jdbcType.getVendorTypeNumber();
    }

}



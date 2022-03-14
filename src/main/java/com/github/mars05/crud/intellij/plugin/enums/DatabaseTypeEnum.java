package com.github.mars05.crud.intellij.plugin.enums;

public enum DatabaseTypeEnum {
    MYSQL("mysql", "MySQL"),
    PG_SQL("pgsql", "PostgreSQL"),
    ORACLE("oracle", "Oracle"),
    SQL_SERVER("sqlserver", "SQL Server"),
    ;
    public static final String ENUM_DESC = "数据库类型 mysql:MySQL,pgsql:PostgreSQL,oracle:Oracle,sqlserver:SQL Server";

    private String code;
    private String desc;

    DatabaseTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DatabaseTypeEnum findByCode(String code) {
        for (DatabaseTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}

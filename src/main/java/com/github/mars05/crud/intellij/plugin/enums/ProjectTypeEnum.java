package com.github.mars05.crud.intellij.plugin.enums;

/**
 * ProjectTypeEnum
 */
public enum ProjectTypeEnum {

    JAVA(1, "Java项目"),
    MAVEN(2, "Maven项目"),
    ;
    private int code;
    private String desc;

    ProjectTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}

package com.github.mars05.crud.intellij.plugin.enums;

/**
 * FileTemplateTypeEnum
 *
 * @author xiaoyu
 */
public enum FileTemplateTypeEnum {
    //
    GENERAL(1, "普通模板"),
    CODE(2, "代码模板"),
    ;
    private int code;
    private String desc;

    public static final String ENUM_DESC = "文件模板类型 1:普通模板 2:代码模板";

    FileTemplateTypeEnum(int code, String desc) {
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

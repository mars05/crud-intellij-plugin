package com.github.mars05.crud.intellij.plugin.model;

/**
 * @author xiaoyu
 */
public class Column {
    private String comment;
    private String name;
    private int type;
    private boolean id;

    /**
     * @param comment 列注释
     * @param name    列名
     * @param type    列类型
     */
    public Column(String comment, String name, int type) {
        this.comment = comment;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public boolean isId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setId(boolean id) {
        this.id = id;
    }
}

package com.github.mars05.crud.intellij.plugin.icon;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, CrudIcons.class);
    }

    public static final Icon CRUD_ICON = load("/icons/crud.png");
    public static final Icon SPRING_BOOT = load("/icons/SpringBoot.png");

    public static final Icon MYSQL_CONN = load("/icons/mysql.png");
    public static final Icon MYSQL_DB = load("/icons/schema.png");
    public static final Icon MYSQL_TABLE = load("/icons/table.png");

    public static final Icon ADD = load("/general/add.png");
    public static final Icon REMOVE = load("/general/remove.png");
    public static final Icon EDIT = load("/actions/edit.png");

    public static final Icon LOADING = load("/icons/loading.gif");
}

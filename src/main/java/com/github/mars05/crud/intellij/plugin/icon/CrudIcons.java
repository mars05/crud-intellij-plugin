package com.github.mars05.crud.intellij.plugin.icon;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author xiaoyu
 * @see AllIcons
 */
public class CrudIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, CrudIcons.class);
    }

    public static final Icon LOGO = load("/icons/logo.png");
    public static final Icon SPRING_BOOT = load("/icons/SpringBoot.png");

    public static final Icon MYSQL_CONN = load("/providers/mysql.png");
    public static final Icon PGSQL_CONN = load("/providers/postgresql.png");
    public static final Icon ORACLE_CONN = load("/providers/oracle.png");

    public static final Icon DB = load("/icons/db/dbms.svg");
    public static final Icon SCHEMA = load("/icons/db/schema.svg");
    public static final Icon TABLE = load("/icons/db/table.svg");

    public static final Icon ADD = load("/general/add.png");
    public static final Icon REMOVE = load("/general/remove.png");
    public static final Icon EDIT = load("/actions/edit.png");

    public static final Icon LOADING = load("/icons/loading.gif");
}

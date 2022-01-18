package com.github.mars05.crud.intellij.plugin.model;

import lombok.Data;

/**
 * 数据源
 */
@Data
public class DataSourceVO {

    /**
     * 数据源ID
     */
    private String id;

    /**
     * 数据库类型
     */
    private String databaseType;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    private String catalog;
    /**
     * 数据库
     */
    private String database;

    private String option;

}
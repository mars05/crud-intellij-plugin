package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

/**
 * 数据源
 */
@Data
public class DataSourceDTO {
    /**
     * 数据源ID
     */
    private Long id;

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

    /**
     * 数据库
     */
    private String database;

    /**
     * 模式
     */
    private String schema;

    private String option;

    private String sid;

}
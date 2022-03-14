package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;

/**
 * 数据源
 */
@Data
public class DataSourceRespDTO {

    /**
     * 数据源ID
     */
    private Long id;

    /**
     * 数据库类型 mysql:MySQL,pgsql:PostgreSQL,oracle:Oracle,sqlserver:SQL Server
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

}
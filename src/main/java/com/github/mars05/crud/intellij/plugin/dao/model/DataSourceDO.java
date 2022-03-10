package com.github.mars05.crud.intellij.plugin.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据源
 */
@Getter
@Setter
@Accessors(chain = true)
public class DataSourceDO extends BaseDO {

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

}
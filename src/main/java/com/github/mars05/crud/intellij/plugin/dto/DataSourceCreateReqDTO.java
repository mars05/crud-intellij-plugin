package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.valid.Enum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 数据源
 */
@Data
public class DataSourceCreateReqDTO {

    /**
     * 数据库类型
     */
    @NotNull
    @Enum(DatabaseTypeEnum.class)
    private String databaseType;

    /**
     * 数据源名称
     */
    @NotEmpty
    private String name;

    /**
     * 主机
     */
    @NotEmpty
    private String host;

    /**
     * 端口
     */
    @NotNull
    private Integer port;

    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * 密码
     */
    @NotEmpty
    private String password;

    /**
     * 数据库
     */
    private String database;

}
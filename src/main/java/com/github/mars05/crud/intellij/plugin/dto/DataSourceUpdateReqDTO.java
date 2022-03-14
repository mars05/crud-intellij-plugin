package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 数据源
 */
@Setter
@Getter
public class DataSourceUpdateReqDTO extends DataSourceCreateReqDTO {
    @NotNull
    private Long id;
}
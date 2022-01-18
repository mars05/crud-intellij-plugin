package com.github.mars05.crud.intellij.plugin.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author xiaoyu
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseDO {
    private String id;
}

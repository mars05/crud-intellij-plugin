package com.github.mars05.crud.intellij.plugin.dao.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class FileTemplateDO extends BaseDO {
    private String id;
    private String path;
    private String content;
    private String projectTemplateId;
    /**
     * 文件类型 1:普通文件 2:代码文件
     */
    private Integer type;
}

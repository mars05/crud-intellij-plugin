package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.intellij.plugin.util.Constants;
import com.github.mars05.crud.intellij.plugin.valid.Contain;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class FileTemplateSaveDTO {
    @NotEmpty
    private String path;
    private String content;
    /**
     * 模板类型 1:普通模板 2:代码模板
     */
    @NotNull
    @Contain(ints = {Constants.FILE_TEMPLATE_TYPE_GENERAL, Constants.FILE_TEMPLATE_TYPE_CODE})
    private Integer type;
}

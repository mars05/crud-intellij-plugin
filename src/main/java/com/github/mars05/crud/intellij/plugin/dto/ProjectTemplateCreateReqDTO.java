package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.intellij.plugin.enums.ProjectTypeEnum;
import com.github.mars05.crud.intellij.plugin.valid.Enum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ProjectTemplateCreateReqDTO {
    @NotEmpty
    private String name;
    /**
     * 项目类型
     */
    @NotNull
    @Enum(ProjectTypeEnum.class)
    private Integer projectType;

    public List<FileTemplateSaveDTO> fileTemplateList;
}

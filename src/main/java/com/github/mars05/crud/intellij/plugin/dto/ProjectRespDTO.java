package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProjectRespDTO {
    private String projectName;
    private List<FileRespDTO> files;
}

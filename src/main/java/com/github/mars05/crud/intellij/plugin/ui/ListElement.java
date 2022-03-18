package com.github.mars05.crud.intellij.plugin.ui;

import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateDTO;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class ListElement {
    private Icon icon;
    private Long id;
    private String name;

    private ProjectTemplateDTO projectTemplateDTO;

    public ListElement(Icon icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public void setProjectTemplateDTO(ProjectTemplateDTO projectTemplateDTO) {
        this.projectTemplateDTO = projectTemplateDTO;
    }

    public ProjectTemplateDTO getProjectTemplateDTO() {
        return projectTemplateDTO;
    }

    public ListElement(Icon icon, Long id, String name) {
        this.icon = icon;
        this.id = id;
        this.name = name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

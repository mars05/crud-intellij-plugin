package com.github.mars05.crud.intellij.plugin.ui;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class ListElement {
    private Icon icon;
    private String name;

    public ListElement(Icon icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Icon getIcon() {
        return icon;
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

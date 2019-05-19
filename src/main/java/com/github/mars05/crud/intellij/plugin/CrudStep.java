package com.github.mars05.crud.intellij.plugin;

import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public interface CrudStep {
    JComponent getComponent();

    boolean validate() throws ConfigurationException;

    void finish();
}

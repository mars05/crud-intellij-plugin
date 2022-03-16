package com.github.mars05.crud.intellij.plugin.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudConfigurable implements Configurable {
    private JPanel myMainPanel;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Crud";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
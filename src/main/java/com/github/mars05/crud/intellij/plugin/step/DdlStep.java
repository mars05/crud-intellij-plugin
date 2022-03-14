package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.ui.LabelWithTooltip;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class DdlStep extends ModuleWizardStep {
    private JTextArea ddlTextArea;
    private LabelWithTooltip ddlLabel;
    private JPanel myMainPanel;

    @Override
    public JComponent getComponent() {
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean isStepVisible() {
        return CrudSettings.isDdl();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        return super.validate();
    }
}

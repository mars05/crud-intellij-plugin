package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class DataSelectStep extends ModuleWizardStep {
    private ButtonGroup buttonGroup = new ButtonGroup();

    private JRadioButton tableRadioButton;
    private JRadioButton ddlRadioButton;
    private JPanel myMainPanel;

    @Override
    public JComponent getComponent() {
        buttonGroup.add(tableRadioButton);
        buttonGroup.add(ddlRadioButton);
        if (CrudSettings.isDdl()) {
            tableRadioButton.setSelected(false);
            ddlRadioButton.setSelected(true);
        } else {
            tableRadioButton.setSelected(true);
            ddlRadioButton.setSelected(false);
        }
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {
        CrudSettings.setDdl(ddlRadioButton.isSelected());
        return super.validate();
    }
}

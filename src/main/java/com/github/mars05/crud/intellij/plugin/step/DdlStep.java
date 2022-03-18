package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.util.SqlUtils;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class DdlStep extends ModuleWizardStep {
    private JTextArea ddlTextArea;
    private JPanel myMainPanel;
    private JScrollPane myScrollPane;

    @Override
    public JComponent getComponent() {
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean isStepVisible() {
        return CrudSettings.currentGenerate().isDdlSelected();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (StringUtils.isBlank(ddlTextArea.getText())) {
            throw new ConfigurationException("DDL不能为空", "校验失败");
        }
        try {
            SqlUtils.getTablesByDdl(ddlTextArea.getText(), DatabaseTypeEnum.MYSQL);
        } catch (Exception exception) {
            throw new ConfigurationException(exception.getMessage(), "DDL解析错误");
        }
        CrudSettings.currentGenerate().setDdl(ddlTextArea.getText());
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
    }
}

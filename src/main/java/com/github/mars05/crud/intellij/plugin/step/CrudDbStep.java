package com.github.mars05.crud.intellij.plugin.step;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.hub.common.enums.DatabaseTypeEnum;
import com.github.mars05.crud.hub.common.service.DataSourceService;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudDbStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private CrudList myDbList;
    private JLabel myDbLabel;
    private JLabel myPathLabel;
    private JScrollPane myScrollPane;

    private DataSourceService dataSourceService = CrudUtils.getBean(DataSourceService.class);

    @Override
    public JComponent getComponent() {
        getList();
        return myMainPanel;
    }

    @Override
    public boolean isStepVisible() {
        Long dsId = CrudSettings.currentGenerate().getDsId();
        if (dsId == null) {
            return false;
        }
        return CollectionUtils.isEmpty(CrudSettings.currentGenerate().getModelTables())
                && !CrudSettings.currentGenerate().isDdlSelected()
                && !DatabaseTypeEnum.ORACLE.getCode().equals(dataSourceService.detail(dsId).getDatabaseType());
    }

    private void getList() {
        if (CrudSettings.currentGenerate().getDsId() != null) {
            myDbList.clearElement();
            for (String name : dataSourceService.allDatabase(CrudSettings.currentGenerate().getDsId())) {
                myDbList.addElement(new ListElement(CrudIcons.DB, name));
            }
        }
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            ListElement listElement = myDbList.getSelectedElement();
            if (listElement == null) {
                throw new Exception("请选择一个数据库");
            }
            CrudSettings.currentGenerate().setDatabase(listElement.getName());
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "数据库打开失败");
        }
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
        myDbLabel = new JBLabel();
    }
}

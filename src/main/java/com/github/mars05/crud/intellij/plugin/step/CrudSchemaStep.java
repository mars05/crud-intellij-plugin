package com.github.mars05.crud.intellij.plugin.step;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.hub.common.enums.DatabaseTypeEnum;
import com.github.mars05.crud.hub.common.service.DataSourceService;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
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
public class CrudSchemaStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private CrudList mySchemaList;
    private JLabel mySchemaLabel;
    private JLabel myPathLabel;
    private JScrollPane myScrollPane;

    private final DataSourceService dataSourceService = CrudUtils.getBean(DataSourceService.class);

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
                && (DatabaseTypeEnum.PG_SQL.getCode().equals(dataSourceService.detail(dsId).getDatabaseType())
                || DatabaseTypeEnum.ORACLE.getCode().equals(dataSourceService.detail(dsId).getDatabaseType()));
    }

    private void getList() {
        GenerateDTO generateDTO = CrudSettings.currentGenerate();
        if (generateDTO.getDsId() != null) {
            mySchemaList.clearElement();
            for (String name : dataSourceService.allSchema(generateDTO.getDsId(), generateDTO.getDatabase())) {
                mySchemaList.addElement(new ListElement(CrudIcons.SCHEMA, name));
            }
        }
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            ListElement listElement = mySchemaList.getSelectedElement();
            if (listElement == null) {
                throw new Exception("请选择一个模式");
            }
            CrudSettings.currentGenerate().setSchema(listElement.getName());
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "数据库打开失败");
        }
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
        mySchemaLabel = new JBLabel();
    }
}

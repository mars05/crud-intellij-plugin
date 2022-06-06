package com.github.mars05.crud.intellij.plugin.step;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.service.DataSourceService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
public class CrudTableStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private CrudList myTableList;
    private JLabel myTableLabel;
    private JLabel myPathLabel;
    private JScrollPane myScrollPane;

    private DataSourceService dataSourceService = new DataSourceService();

    public CrudTableStep() {
        this.myTableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public JComponent getComponent() {
        getList();
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    private void getList() {
        GenerateDTO generateDTO = CrudSettings.currentGenerate();
        if (generateDTO.getDatabase() != null) {
            myTableList.clearElement();
            List<String> strings;
            if (generateDTO.getSchema() != null) {
                strings = dataSourceService.allTableName(generateDTO.getDsId(), generateDTO.getDatabase(), generateDTO.getSchema());
            } else {
                strings = dataSourceService.allTableName(generateDTO.getDsId(), generateDTO.getDatabase());
            }
            for (String name : strings) {
                myTableList.addElement(new ListElement(CrudIcons.TABLE, name));
            }
        }
    }

    @Override
    public boolean isStepVisible() {
        return CollectionUtils.isEmpty(CrudSettings.currentGenerate().getModelTables()) && !CrudSettings.currentGenerate().isDdlSelected();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            List<ListElement> elements = myTableList.getSelectedElementList();
            if (elements == null || elements.size() == 0) {
                throw new Exception("请选择至少一个表");
            }
            CrudSettings.currentGenerate().setTables(elements.stream().map(ListElement::getName).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "表选择失败");
        }
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
        myTableLabel = new JBLabel();
    }
}

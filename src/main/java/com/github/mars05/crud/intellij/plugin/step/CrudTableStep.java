package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.hub.common.dto.DataSourceDTO;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.model.Table;
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

    private final DataSourceService dataSourceService = CrudUtils.getBean(DataSourceService.class);

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
        DataSourceDTO dataSource = CrudSettings.currentGenerate().getDataSource();
        if (dataSource == null) {
            return;
        }
        if (dataSource.getDatabase() != null || dataSource.getSchema() != null) {
            myTableList.clearElement();
            List<String> strings;
            if (dataSource.getSchema() != null) {
                strings = dataSourceService.allTableName(dataSource.getId(), dataSource.getDatabase(), dataSource.getSchema());
            } else {
                strings = dataSourceService.allTableName(dataSource.getId(), dataSource.getDatabase());
            }
            for (String name : strings) {
                myTableList.addElement(new ListElement(CrudIcons.TABLE, name));
            }
        }
    }

    @Override
    public boolean isStepVisible() {
        return 1 == CrudSettings.currentGenerate().getTableSource();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            List<ListElement> elements = myTableList.getSelectedElementList();
            if (elements == null || elements.size() == 0) {
                throw new Exception("请选择至少一个表");
            }
            List<String> tableNameList = elements.stream().map(ListElement::getName).collect(Collectors.toList());

            DataSourceDTO dataSource = CrudSettings.currentGenerate().getDataSource();
            List<Table> tables = dataSourceService.getTables(dataSource.getId(), dataSource.getDatabase(), dataSource.getSchema(), tableNameList);

            List<String> errorList = tableNameList.stream().filter(name -> !tables.stream().map(Table::getTableName)
                    .collect(Collectors.toList()).contains(name)
            ).collect(Collectors.toList());
            if (!errorList.isEmpty()) {
                throw new BizException(errorList + "表获取失败");
            }

            CrudSettings.currentGenerate().setTables(tables);
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

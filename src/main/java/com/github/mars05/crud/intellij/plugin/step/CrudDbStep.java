package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.ui.CrudDbView;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.github.mars05.crud.intellij.plugin.util.DbHelper;
import com.github.mars05.crud.intellij.plugin.util.SelectionContext;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoyu
 */
public class CrudDbStep extends ModuleWizardStep {

    private CrudDbView myCrudDbView;
    private CrudTableStep myTableStep;


    public CrudDbStep(CrudDbView crudDbView, CrudTableStep tableStep) {
        myCrudDbView = crudDbView;
        myTableStep = tableStep;
    }

    @Override
    public JComponent getComponent() {
        return myCrudDbView.getComponent();
    }

    @Override
    public void updateDataModel() {

    }

    public void reloadListElements(List<ListElement> listElements) {
        CrudList crudList = myCrudDbView.getCrudList();
        crudList.clearElement();

        if (listElements != null) {
            for (ListElement listElement : listElements) {
                crudList.addElement(listElement);
            }
        }
    }

    public void reloadPathLabel(String str) {
        myCrudDbView.getPathLabel().setText(str);
    }

    private DbHelper dbHelper;

    public void reloadDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public boolean validate() throws ConfigurationException {

        try {
            int index = myCrudDbView.getCrudList().getSelectedIndex();
            if (index == -1) {
                throw new Exception("请选择一个数据库");
            }
            String name = myCrudDbView.getCrudList().getSelectedElement().getName();
            List<String> allTableName = dbHelper.getAllTableName(name);

            List<ListElement> ls = new ArrayList<>();
            for (String tableName : allTableName) {
                ls.add(new ListElement(CrudIcons.MYSQL_TABLE, tableName));
            }
            myTableStep.reloadListElements(ls);
            myTableStep.reloadPathLabel(myCrudDbView.getPathLabel().getText() + " " + name + " >");
            myTableStep.reloadDbHelper(dbHelper);
            SelectionContext.setDb(name);
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "数据库打开失败");
        }
        return super.validate();
    }
}

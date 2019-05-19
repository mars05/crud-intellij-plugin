package com.github.mars05.crud.intellij.plugin.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudTableView implements CrudView {
    private JPanel myMainPanel;
    private JList myTableList;
    private JLabel myTableLabel;
    private JLabel myPathLabel;
    private JScrollPane myScrollPane;

    @Override
    public CrudList getCrudList() {
        CrudList crudList = (CrudList) this.myTableList;
        crudList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return crudList;
    }

    @Override
    public JComponent getComponent() {
        return myMainPanel;
    }

    public JLabel getPathLabel() {
        return myPathLabel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        myScrollPane = new JBScrollPane();
        myTableLabel = new JBLabel();
    }
}

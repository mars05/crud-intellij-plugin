package com.github.mars05.crud.intellij.plugin.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudDbView implements CrudView {
    private JPanel myMainPanel;
    private JList myDbList;
    private JLabel myDbLabel;
    private JLabel myPathLabel;
    private JScrollPane myScrollPane;

    public CrudList getCrudList() {
        return (CrudList) myDbList;
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
        myDbLabel = new JBLabel();
    }
}

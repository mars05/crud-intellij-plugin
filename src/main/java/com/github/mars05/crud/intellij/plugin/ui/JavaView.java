package com.github.mars05.crud.intellij.plugin.ui;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class JavaView {
    private JPanel myMainPanel;
    private JTextField basePackageTextField;

    public JTextField getBasePackageTextField() {
        return basePackageTextField;
    }

    public JComponent getComponent() {
        return myMainPanel;
    }
}

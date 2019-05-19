package com.github.mars05.crud.intellij.plugin.ui;

import com.github.mars05.crud.intellij.plugin.CrudBundle;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.setting.Conn;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @author xiaoyu
 */
public class CrudAddConnDialog extends DialogWrapper {
    private JPanel myMainPanel;
    private JPanel myConnPanel;
    private JPanel myActionPanel;
    private JLabel myNameLabel;
    private JTextField myNameField;
    private JLabel myHostLabel;
    private JTextField myHostField;
    private JTextField myPortField;
    private JTextField myUsernameField;
    private JPasswordField myPasswordField;
    private JLabel myPortLabel;
    private JLabel myUsernameLabel;
    private JLabel myPasswordLabel;
    private CrudConnView myCrudConnView;

    public CrudAddConnDialog(CrudConnView crudConnView) {
        super(crudConnView.getComponent(), false);
        myCrudConnView = crudConnView;
        setTitle("添加连接");
        init();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        ValidationInfo info = null;
        if (StringUtil.isEmptyOrSpaces(myNameField.getText())) {
            info = new ValidationInfo(CrudBundle.message("conn.validate.name"));
        }
        if (info == null && StringUtil.isEmptyOrSpaces(myHostField.getText())) {
            info = new ValidationInfo(CrudBundle.message("conn.validate.host"));
        }
        String portText = myPortField.getText();
        if (info == null && StringUtil.isEmptyOrSpaces(portText)) {
            info = new ValidationInfo(CrudBundle.message("conn.validate.port"));
        }
        if (info == null && !StringUtils.isNumeric(portText)) {
            info = new ValidationInfo(CrudBundle.message("conn.validate.portnum"));
        }
        if (info == null && StringUtil.isEmptyOrSpaces(myUsernameField.getText())) {
            info = new ValidationInfo(CrudBundle.message("conn.validate.username"));
        }
        if (info == null) {
            if (myPasswordField.getPassword() == null || StringUtil.isEmptyOrSpaces(new String(myPasswordField.getPassword()))) {
                info = new ValidationInfo(CrudBundle.message("conn.validate.password"));
            }
        }
        return info;
    }


    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    protected void doOKAction() {
        CrudList crudList = myCrudConnView.getCrudList();
        crudList.clearElement();
        List<Conn> conns = CrudSettings.getInstance().getConns();

        conns.add(new Conn(myNameField.getText(), myHostField.getText(), Integer.valueOf(myPortField.getText()), myUsernameField.getText(), new String(myPasswordField.getPassword())));

        for (Conn conn : conns) {
            crudList.addElement(new ListElement(CrudIcons.MYSQL_CONN, conn.getName()));
        }
        super.doOKAction();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPanel;
    }
}

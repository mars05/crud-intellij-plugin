package com.github.mars05.crud.intellij.plugin.ui;

import com.github.mars05.crud.intellij.plugin.CrudBundle;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.setting.Conn;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.util.DbHelper;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author xiaoyu
 */
public class CrudEditConnDialog extends DialogWrapper {
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
	private JButton myTestButton;
	private volatile boolean isRepaint = true;
	private CrudConnView myCrudConnView;

	private Conn myConn;


	public CrudEditConnDialog(CrudConnView crudConnView, Conn conn) {
		super(crudConnView.getComponent(), false);
		myCrudConnView = crudConnView;
		setTitle("修改连接");
		myNameField.setText(conn.getName());
		myHostField.setText(conn.getHost());
		myPortField.setText(String.valueOf(conn.getPort()));
		myUsernameField.setText(conn.getUsername());
		myPasswordField.setText(conn.getPassword());
		myConn = conn;

		myTestButton.addActionListener(e -> {
			isRepaint = false;
			Container contentPane = getContentPane();
			Graphics graphics = contentPane.getGraphics().create();
			graphics.setColor(new Color(255, 255, 255, 255 / 3));
			graphics.fillRect(0, 0, contentPane.getWidth(), contentPane.getHeight());
			try {
				Conn connTemp = new Conn(myNameField.getText(), myHostField.getText(), Integer.valueOf(myPortField.getText()), myUsernameField.getText(), new String(myPasswordField.getPassword()));
				DbHelper dbHelper = new DbHelper(connTemp.getHost(), connTemp.getPort(), connTemp.getUsername(), connTemp.getPassword());
				dbHelper.getDatabases();

				Messages.showInfoMessage(myMainPanel, "连接成功", "");
			} catch (Exception ex) {
				Messages.showErrorDialog(myMainPanel, ex.getMessage());
			} finally {
				isRepaint = true;
				graphics.dispose();
				contentPane.repaint();
			}
		});
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
		myConn.setName(myNameField.getText());
		myConn.setHost(myHostField.getText());
		myConn.setPort(Integer.valueOf(myPortField.getText()));
		myConn.setUsername(myUsernameField.getText());
		myConn.setPassword(new String(myPasswordField.getPassword()));

		CrudList crudList = myCrudConnView.getCrudList();
		crudList.clearElement();
		List<Conn> conns = CrudSettings.getInstance().getConns();
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

	private void createUIComponents() {
		// TODO: place custom component creation code here
		myTestButton = new JButton() {
			private static final long serialVersionUID = -137016959157649166L;

			@Override
			public void repaint() {
				if (isRepaint) {
					super.repaint();
				}
			}
		};
	}
}

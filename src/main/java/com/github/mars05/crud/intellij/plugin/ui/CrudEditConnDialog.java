package com.github.mars05.crud.intellij.plugin.ui;

import com.github.mars05.crud.hub.common.dto.DataSourceCreateReqDTO;
import com.github.mars05.crud.hub.common.dto.DataSourceRespDTO;
import com.github.mars05.crud.hub.common.dto.DataSourceUpdateReqDTO;
import com.github.mars05.crud.hub.common.enums.DatabaseTypeEnum;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.service.DataSourceService;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.CrudBundle;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.step.CrudConnStep;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

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
    private JComboBox<DatabaseTypeEnum> databaseTypeComboBox;
    private JTextField myInitDbField;
    private JLabel myInitDbLabel;
    private JTextField mySidField;
    private JLabel mySidLabel;
    private volatile boolean isRepaint = true;
    private CrudConnStep myCrudConnStep;

    private final DataSourceService dataSourceService = CrudUtils.getBean(DataSourceService.class);
    private Long dsId;

    public CrudEditConnDialog(CrudConnStep crudConnStep, Long dsId) {
        super(crudConnStep.getComponent(), false);
        myCrudConnStep = crudConnStep;
        this.dsId = dsId;
        setTitle(dsId == null ? "添加连接" : "修改连接");
        databaseTypeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel jbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                DatabaseTypeEnum typeEnum = (DatabaseTypeEnum) value;
                Icon icon;
                switch (typeEnum) {
                    case MYSQL:
                        icon = CrudIcons.MYSQL_CONN;
                        break;
                    case PG_SQL:
                        icon = CrudIcons.PGSQL_CONN;
                        break;
                    case ORACLE:
                        icon = CrudIcons.ORACLE_CONN;
                        break;
                    default:
                        throw new BizException("暂不支持的数据库类型");
                }
                jbl.setIcon(icon);
                jbl.setText(typeEnum.getDesc());
                return jbl;
            }
        });
        databaseTypeComboBox.addItem(DatabaseTypeEnum.MYSQL);
        databaseTypeComboBox.addItem(DatabaseTypeEnum.PG_SQL);
        databaseTypeComboBox.addItem(DatabaseTypeEnum.ORACLE);
        databaseTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switchDbType();
            }
        });
        switchDbType();
        myTestButton.addActionListener(e -> {
            isRepaint = false;
            Container contentPane = getContentPane();
            Graphics graphics = contentPane.getGraphics().create();
            graphics.setColor(new Color(255, 255, 255, 255 / 3));
            graphics.fillRect(0, 0, contentPane.getWidth(), contentPane.getHeight());
            try {
                DataSourceCreateReqDTO createReqDTO = new DataSourceCreateReqDTO();
                createReqDTO.setDatabaseType(((DatabaseTypeEnum) databaseTypeComboBox.getSelectedItem()).getCode());
                createReqDTO.setName(myNameField.getText());
                createReqDTO.setHost(myHostField.getText());
                createReqDTO.setPort(Integer.valueOf(myPortField.getText()));
                createReqDTO.setInitDb(myInitDbField.getText());
                createReqDTO.setSid(mySidField.getText());
                createReqDTO.setUsername(myUsernameField.getText());
                createReqDTO.setPassword(new String(myPasswordField.getPassword()));
                dataSourceService.testConnection(createReqDTO);
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
            info = new ValidationInfo(CrudBundle.message("validate.conn.name"));
        }
        if (info == null && StringUtil.isEmptyOrSpaces(myHostField.getText())) {
            info = new ValidationInfo(CrudBundle.message("validate.conn.host"));
        }
        String portText = myPortField.getText();
        if (info == null && StringUtil.isEmptyOrSpaces(portText)) {
            info = new ValidationInfo(CrudBundle.message("validate.conn.port"));
        }
        if (info == null && !StringUtils.isNumeric(portText)) {
            info = new ValidationInfo(CrudBundle.message("validate.conn.portnum"));
        }
        if (info == null && StringUtil.isEmptyOrSpaces(myUsernameField.getText())) {
            info = new ValidationInfo(CrudBundle.message("validate.conn.username"));
        }
        if (info == null) {
            if (myPasswordField.getPassword() == null || StringUtil.isEmptyOrSpaces(new String(myPasswordField.getPassword()))) {
                info = new ValidationInfo(CrudBundle.message("validate.conn.password"));
            }
        }
        return info;
    }

    private void switchDbType() {
        if (dsId != null) {
            databaseTypeComboBox.setEnabled(false);
            DataSourceRespDTO respDTO = dataSourceService.detail(dsId);
            databaseTypeComboBox.setSelectedItem(DatabaseTypeEnum.findByCode(respDTO.getDatabaseType()));
            myNameField.setText(respDTO.getName());

            myHostField.setText(respDTO.getHost());
            myPortField.setText(String.valueOf(respDTO.getPort()));
            myInitDbField.setText(respDTO.getInitDb());
            mySidField.setText(respDTO.getSid());
            myUsernameField.setText(respDTO.getUsername());
            myPasswordField.setText(respDTO.getPassword());
        }
        DatabaseTypeEnum typeEnum = (DatabaseTypeEnum) databaseTypeComboBox.getSelectedItem();
        switch (typeEnum) {
            case MYSQL:
                myInitDbLabel.setVisible(false);
                myInitDbField.setVisible(false);
                mySidLabel.setVisible(false);
                mySidField.setVisible(false);
                if (dsId == null) {
                    myHostField.setText("localhost");
                    myPortField.setText("3306");
                    myUsernameField.setText("root");
                }
                break;
            case PG_SQL:
                myInitDbLabel.setVisible(true);
                myInitDbField.setVisible(true);
                mySidLabel.setVisible(false);
                mySidField.setVisible(false);
                if (dsId == null) {
                    myHostField.setText("localhost");
                    myPortField.setText("5432");
                    myInitDbField.setText("postgres");
                    myUsernameField.setText("postgres");
                }
                break;
            case ORACLE:
                myInitDbLabel.setVisible(false);
                myInitDbField.setVisible(false);
                mySidLabel.setVisible(true);
                mySidField.setVisible(true);
                if (dsId == null) {
                    myHostField.setText("localhost");
                    myPortField.setText("1521");
                    mySidField.setText("ORCL");
                    myUsernameField.setText("");
                }
                break;
            default:
                throw new BizException("暂不支持的数据库类型");
        }
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    protected void doOKAction() {
        DataSourceCreateReqDTO reqDTO = new DataSourceCreateReqDTO();
        reqDTO.setDatabaseType(((DatabaseTypeEnum) databaseTypeComboBox.getSelectedItem()).getCode());
        reqDTO.setName(myNameField.getText());
        reqDTO.setHost(myHostField.getText());
        reqDTO.setInitDb(myInitDbField.getText());
        reqDTO.setSid(mySidField.getText());
        reqDTO.setPort(Integer.valueOf(myPortField.getText()));
        reqDTO.setUsername(myUsernameField.getText());
        reqDTO.setPassword(new String(myPasswordField.getPassword()));
        if (dsId == null) {
            dataSourceService.create(reqDTO);
        } else {
            DataSourceUpdateReqDTO updateReqDTO = BeanUtils.convertBean(reqDTO, DataSourceUpdateReqDTO.class);
            updateReqDTO.setId(dsId);
            dataSourceService.update(updateReqDTO);
        }

        myCrudConnStep.getList();
        super.doOKAction();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPanel;
    }

    private void createUIComponents() {
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

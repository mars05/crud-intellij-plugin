package com.github.mars05.crud.intellij.plugin.step;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.hub.common.dto.DataSourceRespDTO;
import com.github.mars05.crud.hub.common.enums.DatabaseTypeEnum;
import com.github.mars05.crud.hub.common.service.DataSourceService;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudEditConnDialog;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author xiaoyu
 */
public class CrudConnStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private JButton myAddConnButton;
    private CrudList myConnsList;
    private JScrollPane myScrollPane;
    private JLabel myConnLabel;
    private JButton myEditConnButton;
    private JButton myRemoveConnButton;

    private final DataSourceService dataSourceService = CrudUtils.getBean(DataSourceService.class);

    public CrudConnStep() {
        myAddConnButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrudEditConnDialog dialog = new CrudEditConnDialog(CrudConnStep.this, null);
                dialog.showAndGet();
            }
        });
        myEditConnButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListElement listElement = myConnsList.getSelectedElement();
                if (listElement == null) {
                    Messages.showErrorDialog(myMainPanel, "请选择要修改的连接");
                    return;
                }
                CrudEditConnDialog dialog = new CrudEditConnDialog(CrudConnStep.this, listElement.getId());
                dialog.showAndGet();
            }
        });
        myRemoveConnButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListElement listElement = myConnsList.getSelectedElement();
                if (listElement == null) {
                    Messages.showErrorDialog(myMainPanel, "请选择要删除的连接");
                    return;
                }
                int result = Messages.showYesNoDialog(listElement.getName(), "确认删除？", Messages.getQuestionIcon());
                if (result == Messages.YES) {
                    dataSourceService.delete(listElement.getId());
                    getList();
                }
            }
        });
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean isStepVisible() {
        return CollectionUtils.isEmpty(CrudSettings.currentGenerate().getModelTables()) && !CrudSettings.currentGenerate().isDdlSelected();

    }

    @Override
    public JComponent getComponent() {
        getList();
        return myMainPanel;
    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            ListElement element = myConnsList.getSelectedElement();
            if (element == null) {
                throw new Exception("请选择一个连接");
            }
            DataSourceRespDTO respDTO = dataSourceService.detail(myConnsList.getSelectedElement().getId());
            CrudSettings.currentGenerate().setDsId(respDTO.getId());

        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "连接打开失败");
        }
        return super.validate();
    }

    public void getList() {
        if (!CrudSettings.currentGenerate().isDdlSelected()) {
            myConnsList.clearElement();
            for (DataSourceRespDTO conn : dataSourceService.list()) {
                Icon icon = null;
                if (DatabaseTypeEnum.MYSQL.getCode().equals(conn.getDatabaseType())) {
                    icon = CrudIcons.MYSQL_CONN;
                } else if (DatabaseTypeEnum.PG_SQL.getCode().equals(conn.getDatabaseType())) {
                    icon = CrudIcons.PGSQL_CONN;
                } else if (DatabaseTypeEnum.ORACLE.getCode().equals(conn.getDatabaseType())) {
                    icon = CrudIcons.ORACLE_CONN;
                }
                myConnsList.addElement(new ListElement(icon, conn.getId(), conn.getName()));
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        myScrollPane = new JBScrollPane();
        myConnLabel = new JBLabel();
    }
}

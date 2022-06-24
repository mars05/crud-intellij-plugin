package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.hub.common.enums.DatabaseTypeEnum;
import com.github.mars05.crud.hub.common.util.SqlUtils;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class DdlStep extends ModuleWizardStep {
    private JTextArea ddlTextArea;
    private JPanel myMainPanel;
    private JScrollPane myScrollPane;

    @Override
    public JComponent getComponent() {
        ddlTextArea.setText("CREATE TABLE `demo` (\n" +
                "  `demo_id` bigint NOT NULL COMMENT '主键ID',\n" +
                "  `demo_name` varchar(255) DEFAULT NULL COMMENT '名称',\n" +
                "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  `update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`demo_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Demo';");
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean isStepVisible() {
        return 2 == CrudSettings.currentGenerate().getTableSource();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (StringUtils.isBlank(ddlTextArea.getText())) {
            throw new ConfigurationException("DDL不能为空", "校验失败");
        }
        try {
            SqlUtils.getTablesByDdl(ddlTextArea.getText(), DatabaseTypeEnum.MYSQL);
        } catch (Exception exception) {
            throw new ConfigurationException(exception.getMessage(), "DDL解析错误");
        }
        CrudSettings.currentGenerate().setTables(SqlUtils.getTablesByDdl(ddlTextArea.getText(),
                DatabaseTypeEnum.MYSQL));
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
    }
}

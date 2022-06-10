package com.github.mars05.crud.intellij.plugin.step;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mars05.crud.hub.common.model.Column;
import com.github.mars05.crud.hub.common.model.Table;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ListWithSelection;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.table.ComboBoxTableCellEditor;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
public class PrimaryKeySelectStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private JScrollPane myScrollPane;
    private JBTable myTable;
    private MyTableModel myTableModel = new MyTableModel();

    @Override
    public JComponent getComponent() {
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean isStepVisible() {
        return CollectionUtils.isNotEmpty(CrudSettings.currentGenerate().getModelTables());
    }

    @Override
    public boolean validate() throws ConfigurationException {
        List<Table> modelTables = CrudSettings.currentGenerate().getModelTables();
        for (Table t : modelTables) {
            boolean key = false;
            for (Column column : t.getColumns()) {
                if (column.getPrimaryKey() != null && column.getPrimaryKey()) {
                    key = true;
                    break;
                }
            }
            if (!key) {
                throw new ConfigurationException("[" + t.getTableName() + "]表没有主键");
            }
        }
        return super.validate();
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
        myTable = new MyTable();
    }

    private static Vector<Vector<String>> getData() {
        GenerateDTO generateDTO = CrudSettings.currentGenerate();
        if (generateDTO.getModelTables() != null) {
            Vector<Vector<String>> dataVector = new Vector<>();
            for (Table modelTable : generateDTO.getModelTables()) {
                Vector<String> v = new Vector<>();
                v.add(modelTable.getTableName());
                dataVector.add(v);
            }
            return dataVector;
        }
        return null;
    }

    public static class MyTable extends JBTable {
        public MyTable() {
            super(new MyTableModel());
            setSelectionBackground(getBackground());
            final TableColumn displayTypeColumn = getColumnModel().getColumn(1);
            displayTypeColumn.setCellRenderer(ComboBoxTableCellRenderer.INSTANCE);
            displayTypeColumn.setCellEditor(new ComboBoxTableCellEditor() {

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    JComboBox comboBox = (JComboBox) getComponent();
                    ItemListener[] itemListeners = comboBox.getItemListeners();
                    for (ItemListener itemListener : itemListeners) {
                        if (itemListener instanceof MyItemListener) {
                            comboBox.removeItemListener(itemListener);
                        }
                    }
                    comboBox.addItemListener(new MyItemListener(row));
                    ListWithSelection<String> options = new ListWithSelection<>(CrudSettings.currentGenerate()
                            .getModelTables().get(row).getColumns().stream()
                            .map(Column::getColumnName).collect(Collectors.toList()),
                            CrudSettings.currentGenerate()
                                    .getModelTables().get(row).getColumns().stream().filter(Column::getPrimaryKey)
                                    .map(Column::getColumnName).findFirst().orElse(null)
                    );
                    //noinspection unchecked
                    comboBox.setModel(new ListComboBoxModel(options));
                    if (!options.isEmpty()) {
                        comboBox.setSelectedItem(options.getSelection());
                    }
                    return comboBox;
                }
            });
        }

        private static class MyItemListener implements ItemListener {
            private final int row;

            public MyItemListener(int row) {
                this.row = row;
            }

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Table t = CrudSettings.currentGenerate()
                            .getModelTables().get(row);
                    t.getColumns().forEach(c -> c.setPrimaryKey(false));
                    t.getColumns().stream().filter(c -> c.getColumnName().equals(e.getItem()))
                            .forEach(c -> c.setPrimaryKey(true));
                }
            }
        }
    }

    private static class MyTableModel extends DefaultTableModel {

        public MyTableModel() {
            super(getData(), new Vector<>(Arrays.asList("表", "主键")));
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    }

    private static class ComboBoxTableCellRenderer extends JPanel implements TableCellRenderer {
        public static final TableCellRenderer INSTANCE = new ComboBoxTableCellRenderer();

        private static final Logger LOG = Logger.getInstance("#com.intellij.util.ui.ComboBoxTableCellRenderer");

        private final JComboBox myCombo = new ComboBox();

        private ComboBoxTableCellRenderer() {
            super(new GridBagLayout());
            add(myCombo,
                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBUI.emptyInsets(), 0, 0));
        }

        @Override
        public JComponent getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComboBox comboBox = myCombo;
            ListWithSelection<String> options = new ListWithSelection<>(CrudSettings.currentGenerate()
                    .getModelTables().get(row).getColumns().stream()
                    .map(Column::getColumnName).collect(Collectors.toList()),
                    value != null ? value.toString() :
                            CrudSettings.currentGenerate()
                                    .getModelTables().get(row).getColumns().stream().filter(Column::getPrimaryKey)
                                    .map(Column::getColumnName).findFirst().orElse(null)
            );
            //noinspection unchecked
            comboBox.setModel(new ListComboBoxModel(options));
            if (!options.isEmpty()) {
                comboBox.setSelectedItem(options.getSelection());
            }
            return this;
        }
    }
}

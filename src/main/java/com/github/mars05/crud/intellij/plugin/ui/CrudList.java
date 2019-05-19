package com.github.mars05.crud.intellij.plugin.ui;

import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.SpeedSearchComparator;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;

/**
 * @author xiaoyu
 */
public class CrudList extends JBList {
    private DefaultListModel model = new DefaultListModel();

    public CrudList() {
        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel jbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                ListElement ele = (ListElement) value;
                jbl.setIcon(ele.getIcon());
                jbl.setText(ele.getName());
                return jbl;
            }
        });
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(model);
        new ListSpeedSearch<ListElement>(this) {
            @Override
            protected String getElementText(Object element) {
                return ((ListElement) element).getName();
            }
        }.setComparator(new SpeedSearchComparator(false));
    }

    public ListElement getSelectedElement() {
        return (ListElement) model.getElementAt(getSelectedIndex());
    }

    public void addElement(ListElement element) {
        model.add(model.getSize(), element);
    }

    public void clearElement() {
        model.clear();
    }


}

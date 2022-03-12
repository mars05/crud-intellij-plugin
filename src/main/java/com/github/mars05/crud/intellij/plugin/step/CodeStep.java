package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.FileTemplateDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.enums.FileTemplateTypeEnum;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CheckBoxList;
import com.intellij.util.ui.ThreeStateCheckBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
public class CodeStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private JTextField basePackageTextField;
    private JScrollPane myScrollPane;
    private ThreeStateCheckBox allCheckBox;
    private CheckBoxList checkBoxList;

    private List<String> nameList = new ArrayList<>();
    private Long ptId;

    private final ProjectTemplateService projectTemplateService = ServiceManager.getService(ProjectTemplateService.class);

    public CodeStep() {
        allCheckBox.addActionListener(e -> {
            if (nameList.size() == getSelectedNameList().size()) {
                allCheckBox.setState(ThreeStateCheckBox.State.NOT_SELECTED);
                nameList.forEach(s -> checkBoxList.setItemSelected(s, false));
            } else {
                allCheckBox.setState(ThreeStateCheckBox.State.SELECTED);
                nameList.forEach(s -> checkBoxList.setItemSelected(s, true));
            }
            checkBoxList.repaint();
        });
        checkBoxList.setCheckBoxListListener((index, value) -> {
            if (nameList.size() == getSelectedNameList().size()) {
                allCheckBox.setState(ThreeStateCheckBox.State.SELECTED);
            } else if (getSelectedNameList().size() > 0) {
                allCheckBox.setState(ThreeStateCheckBox.State.DONT_CARE);
            } else {
                allCheckBox.setState(ThreeStateCheckBox.State.NOT_SELECTED);
            }
        });
    }

    @Override
    public JComponent getComponent() {
        getList();
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() throws ConfigurationException {
        try {
            if (StringUtil.isEmptyOrSpaces(basePackageTextField.getText())) {
                throw new Exception("请输入basePackage");
            }
            if (getSelectedNameList().isEmpty()) {
                throw new Exception("请选择需要生成的代码");
            }
            CodeGenerateReqDTO codeGenerateReqDTO = CrudSettings.getGenerate();
            codeGenerateReqDTO.setNameList(getSelectedNameList());
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "验证失败");
        }
        return super.validate();
    }

    private void getList() {
        CodeGenerateReqDTO codeGenerateReqDTO = CrudSettings.getGenerate();
        if (codeGenerateReqDTO.getPtId() != null && !codeGenerateReqDTO.getPtId().equals(ptId)) {
            codeGenerateReqDTO.setNameList(new ArrayList<>());
            ptId = codeGenerateReqDTO.getPtId();
            checkBoxList.clear();

            ProjectTemplateRespDTO projectTemplateRespDTO = projectTemplateService.detail(codeGenerateReqDTO.getPtId());
            nameList = projectTemplateRespDTO.getFileTemplateList().stream().filter(fileTemplateDTO -> fileTemplateDTO.getType() == FileTemplateTypeEnum.CODE.getCode())
                    .map(FileTemplateDTO::getName).collect(Collectors.toList());
            nameList.forEach(s -> checkBoxList.addItem(s, s, true));
            allCheckBox.setState(ThreeStateCheckBox.State.SELECTED);
        }
    }

    private void createUIComponents() {
        checkBoxList = new CheckBoxList();
    }

    private List<String> getSelectedNameList() {
        return nameList.stream().filter(s ->
                checkBoxList.isItemSelected(s)
        ).collect(Collectors.toList());
    }
}

package com.github.mars05.crud.intellij.plugin.step;

import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class MyTemplateStep extends ModuleWizardStep {
    private JPanel myMainPanel;
    private CrudList templateList;

    private Long ptId;

    private final ProjectTemplateService projectTemplateService = ServiceManager.getService(ProjectTemplateService.class);

    public MyTemplateStep() {

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
            ProjectTemplateRespDTO projectTemplateRespDTO = getSelectedProjectTemplate();
            if (projectTemplateRespDTO == null) {
                throw new Exception("请选择一个项目");
            }
            ptId = projectTemplateRespDTO.getId();
            if (CrudSettings.getGenerate() instanceof ProjectGenerateReqDTO) {
                ProjectGenerateReqDTO projectGenerateReqDTO = CrudSettings.getGenerate();
                projectGenerateReqDTO.setPtId(ptId);
            } else {
                CodeGenerateReqDTO codeGenerateReqDTO = CrudSettings.getGenerate();
                codeGenerateReqDTO.setPtId(ptId);
            }
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "验证失败");
        }
        return super.validate();
    }

    private void getList() {
        if (ptId == null) {
            templateList.clearElement();
            for (ProjectTemplateRespDTO projectTemplateDTO : projectTemplateService.list()) {
                templateList.addElement(new ListElement(null,
                        projectTemplateDTO.getId(), projectTemplateDTO.getName() + "（" + projectTemplateDTO.getOrganizationName() + "）"));
            }
        }
    }

    public ProjectTemplateRespDTO getSelectedProjectTemplate() {
        ListElement selectedElement = templateList.getSelectedElement();
        if (selectedElement == null) {
            return null;
        }
        return projectTemplateService.detail(selectedElement.getId());
    }

}

package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.rpc.HubClient;
import com.github.mars05.crud.intellij.plugin.rpc.request.MarketplaceGetRequest;
import com.github.mars05.crud.intellij.plugin.rpc.request.TokenGetRequest;
import com.github.mars05.crud.intellij.plugin.rpc.response.ProjectTemplateResponse;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaoyu
 */
public class MyTemplateConfigurable implements SearchableConfigurable {
    private static MyTemplateConfigurable myTemplateConfigurable;
    private JPanel myMainPanel;
    private CrudList templateList;
    private JButton refreshButton;
    private JButton deleteButton;

    private final AtomicBoolean isModified = new AtomicBoolean(false);

    private final HubClient hubClient = new HubClient();

    private final ProjectTemplateService projectTemplateService = ServiceManager.getService(ProjectTemplateService.class);

    public MyTemplateConfigurable() {
        refreshButton.addActionListener(e -> {
            ListElement selectedElement = templateList.getSelectedElement();
            if (selectedElement == null) {
                Messages.showErrorDialog("请选择模板", "错误");
                return;
            }
            try {
                ProjectTemplateRespDTO respDTO = projectTemplateService.detail(selectedElement.getId());
                ProjectTemplateResponse response;
                if (respDTO.getPublicFlag() == 1) {
                    response = hubClient.execute(new MarketplaceGetRequest().setId(respDTO.getId()));
                } else {
                    response = hubClient.execute(new TokenGetRequest().setAccessToken(respDTO.getAccessToken()));
                }
                if (!response.isSuccess()) {
                    throw new BizException(response.getMessage());
                }
                int result = Messages.showYesNoDialog(selectedElement.getName(), "确认刷新？", Messages.getQuestionIcon());
                if (result == Messages.YES) {
                    projectTemplateService.update(response.getProjectTemplate());
                    Messages.showInfoMessage("刷新成功", "提示");
                    getList();
                    isModified.set(true);
                }
            } catch (Exception exception) {
                Messages.showErrorDialog(exception.getMessage(), "错误");
            }
        });

        deleteButton.addActionListener(e -> {
            ListElement selectedElement = templateList.getSelectedElement();
            if (selectedElement == null) {
                Messages.showErrorDialog("请选择模板", "错误");
                return;
            }
            try {
                int result = Messages.showYesNoDialog(selectedElement.getName(), "确认删除？", Messages.getQuestionIcon());
                if (result == Messages.YES) {
                    projectTemplateService.delete(selectedElement.getId());
                    Messages.showInfoMessage("删除成功", "提示");
                    getList();
                    isModified.set(true);
                }
            } catch (Exception exception) {
                Messages.showErrorDialog(exception.getMessage(), "错误");
            }
        });

        templateList.addListSelectionListener(e -> {
//            ListElement selectedElement = templateList.getSelectedElement();
        });
    }

    @NotNull
    @Override
    public String getId() {
        return getClass().toString();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "我的模板";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        getList();
        MyTemplateConfigurable.myTemplateConfigurable = this;
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        return isModified.get();
    }

    @Override
    public void apply() throws ConfigurationException {
        isModified.set(false);
    }

    public void getList() {
        templateList.clearElement();
        for (ProjectTemplateRespDTO projectTemplateDTO : projectTemplateService.list()) {
            templateList.addElement(new ListElement(null,
                    projectTemplateDTO.getId(), projectTemplateDTO.getName() + "（" + projectTemplateDTO.getOrganizationName() + "）"));
        }
    }

    public static void refreshList() {
        myTemplateConfigurable.getList();
    }
}

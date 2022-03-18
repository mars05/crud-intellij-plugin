package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateDTO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.rpc.HubClient;
import com.github.mars05.crud.intellij.plugin.rpc.request.MarketplaceGetRequest;
import com.github.mars05.crud.intellij.plugin.rpc.request.MarketplaceListRequest;
import com.github.mars05.crud.intellij.plugin.rpc.request.TokenGetRequest;
import com.github.mars05.crud.intellij.plugin.rpc.response.MarketplaceListResponse;
import com.github.mars05.crud.intellij.plugin.rpc.response.ProjectTemplateResponse;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import com.github.mars05.crud.intellij.plugin.ui.CrudList;
import com.github.mars05.crud.intellij.plugin.ui.ListElement;
import com.github.mars05.crud.intellij.plugin.util.ThreadUtils;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLoadingPanel;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaoyu
 */
public class TemplateImportConfigurable implements SearchableConfigurable, Disposable {
    private JPanel myMainPanel;
    private CrudList marketplaceList;
    private JScrollPane myScrollPane;
    private JTextField keywordTextField;
    private JButton searchButton;
    private JButton tokenButton;
    private JPanel loadingPanel;
    private JButton importButton;
    private JScrollPane myInfoScrollPane;
    private JLabel nameLabel;
    private JLabel orgLabel;
    private JLabel timeLabel;
    private JLabel creatorLabel;
    private JLabel descLabel;
    private JPanel infoPanel;
    private JScrollPane myDescScrollPane;

    private final AtomicBoolean isModified = new AtomicBoolean(false);

    private final HubClient hubClient = new HubClient();

    private final ProjectTemplateService projectTemplateService = ServiceManager.getService(ProjectTemplateService.class);

    public TemplateImportConfigurable() {
        searchButton.addActionListener(e -> getList(new MarketplaceListRequest()));
        importButton.addActionListener(e -> {
            if (marketplaceList.getSelectedElement() == null) {
                Messages.showErrorDialog("请选择模板", "错误");
                return;
            }
            try {
                ProjectTemplateResponse response = hubClient.execute(new MarketplaceGetRequest().setId(marketplaceList.getSelectedElement().getId()));
                if (!response.isSuccess()) {
                    throw new BizException(response.getMessage());
                }
                projectTemplateService.create(response.getProjectTemplate());
                Messages.showInfoMessage("导入成功", "提示");
                isModified.set(true);
                MyTemplateConfigurable.refreshList();
            } catch (Exception exception) {
                Messages.showErrorDialog(exception.getMessage(), "错误");
            }
        });
        tokenButton.addActionListener(e -> {
            String s = Messages.showInputDialog("令牌", "令牌导入", Messages.getInformationIcon());
            try {
                if (s != null) {
                    ProjectTemplateResponse response = hubClient.execute(new TokenGetRequest().setAccessToken(s));
                    if (!response.isSuccess()) {
                        throw new BizException(response.getMessage());
                    }
                    projectTemplateService.create(response.getProjectTemplate());
                    Messages.showInfoMessage("导入成功", "提示");
                    isModified.set(true);
                }
            } catch (Exception exception) {
                Messages.showErrorDialog(exception.getMessage(), "错误");
            }
        });
        marketplaceList.addListSelectionListener(e -> {
            if (marketplaceList.getSelectedElement() == null || marketplaceList.getSelectedElement().getProjectTemplateDTO() == null) {
                myInfoScrollPane.setVisible(false);
                return;
            }
            ProjectTemplateDTO projectTemplateDTO = marketplaceList.getSelectedElement().getProjectTemplateDTO();
            nameLabel.setText(projectTemplateDTO.getName());
            orgLabel.setText(projectTemplateDTO.getOrganizationName());
            timeLabel.setText(projectTemplateDTO.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            creatorLabel.setText(projectTemplateDTO.getCreateName());
            descLabel.setText(projectTemplateDTO.getDescription());
            myInfoScrollPane.setVisible(true);
            infoPanel.setVisible(true);
        });
    }

    private void createUIComponents() {
        myScrollPane = new JBScrollPane();
        myInfoScrollPane = new JBScrollPane();
        myDescScrollPane = new JBScrollPane();
        myDescScrollPane.setBorder(BorderFactory.createEmptyBorder());
        loadingPanel = new JBLoadingPanel(new BorderLayout(), this);
        loadingPanel.setBorder(myScrollPane.getBorder());
    }

    @NotNull
    @Override
    public String getId() {
        return getClass().toString();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "模板导入";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        getList(new MarketplaceListRequest());
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        return isModified.get();
    }

    private JBLoadingPanel getLoadingPanel() {
        return (JBLoadingPanel) loadingPanel;
    }

    @Override
    public void apply() throws ConfigurationException {
        isModified.set(false);
    }

    @Override
    public void reset() {
        isModified.set(false);
    }

    @Override
    public void dispose() {

    }

    private void startLoading() {
        myScrollPane.setVisible(false);
        myInfoScrollPane.setVisible(false);
        getLoadingPanel().setVisible(true);
        getLoadingPanel().startLoading();
    }

    private void stopLoading() {
        myScrollPane.setVisible(true);
        myInfoScrollPane.setVisible(true);
        infoPanel.setVisible(false);
        getLoadingPanel().setVisible(false);
        getLoadingPanel().stopLoading();
    }

    private void getList(MarketplaceListRequest request) {
        ThreadUtils.execute(() -> {
            long st = System.currentTimeMillis();
            startLoading();
            try {
                MarketplaceListResponse response = hubClient.execute(request);
                if (response.isSuccess() && response.getList() != null) {
                    marketplaceList.clearElement();
                    for (ProjectTemplateDTO projectTemplateDTO : response.getList()) {
                        ListElement listElement = new ListElement(null,
                                projectTemplateDTO.getId(), projectTemplateDTO.getName() + "（" + projectTemplateDTO.getOrganizationName() + "）");
                        listElement.setProjectTemplateDTO(projectTemplateDTO);
                        marketplaceList.addElement(listElement);
                    }
                }
            } finally {
                long sleepTime = 500 - (System.currentTimeMillis() - st);
                if (sleepTime > 0) {
                    ThreadUtils.sleep(sleepTime);
                }
                stopLoading();
            }
        });
    }
}

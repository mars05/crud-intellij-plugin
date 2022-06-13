package com.github.mars05.crud.intellij.plugin.wizard;

import com.github.mars05.crud.hub.common.dto.ProjectGenerateReqDTO;
import com.github.mars05.crud.hub.common.dto.ProjectRespDTO;
import com.github.mars05.crud.hub.common.enums.ProjectTypeEnum;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.service.ProjectService;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.step.*;
import com.github.mars05.crud.intellij.plugin.ui.JavaView;
import com.github.mars05.crud.intellij.plugin.ui.MavenView;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.google.common.base.Preconditions;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.DocumentAdapter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaoyu
 */
public class CrudModuleBuilder extends ModuleBuilder {
    JavaView javaView = new JavaView();
    MavenView mavenView = new MavenView();

    private final ProjectTemplateService projectTemplateService = CrudUtils.getBean(ProjectTemplateService.class);
    private final ProjectService projectService = CrudUtils.getBean(ProjectService.class);

    public CrudModuleBuilder() {
    }

    @Nullable
    @Override
    public String getBuilderId() {
        return getClass().getName();
    }

    @Override
    public Icon getNodeIcon() {
        return CrudIcons.LOGO;
    }

    @Override
    public String getPresentableName() {
        return "Crud";
    }

    @Override
    public String getDescription() {
        return "Crud";
    }

    @Override
    public ModuleType getModuleType() {
        return StdModuleTypes.JAVA;
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdk) {
        return sdk == JavaSdk.getInstance();
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new MyTemplateStep();
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        CrudSettings.newGenerate().setCodeGenerate(false);
        return new ModuleWizardStep[]{
                new DataSelectStep(),
                new DdlStep(),
                new CrudConnStep(),
                new CrudDbStep(),
                new CrudSchemaStep(),
                new CrudTableStep()
        };
    }

    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        ProjectTemplateRespDTO respDTO = projectTemplateService.detail(CrudSettings.currentGenerate().getPtId());

        JTextField moduleNameField = settingsStep.getModuleNameField();
        JTextField groupIdTextField = mavenView.getGroupIdTextField();
        JTextField artifactIdTextField = mavenView.getArtifactIdTextField();
        JTextField basePackageTextField = ProjectTypeEnum.JAVA.getCode() == respDTO.getProjectType() ?
                javaView.getBasePackageTextField() : mavenView.getBasePackageTextField();

        AtomicBoolean moduleInput = new AtomicBoolean(false);
        AtomicBoolean artifactIdInput = new AtomicBoolean(false);
        AtomicBoolean syncInput = new AtomicBoolean(false);

        if (ProjectTypeEnum.JAVA.getCode() == respDTO.getProjectType()) {
            settingsStep.addSettingsComponent(javaView.getComponent());
        }
        if (ProjectTypeEnum.MAVEN.getCode() == respDTO.getProjectType()) {
            settingsStep.addSettingsComponent(mavenView.getComponent());
            if (moduleNameField != null) {
                artifactIdTextField.setText(moduleNameField.getText());
                basePackageTextField.setText(groupIdTextField.getText()
                        + "." + artifactIdTextField.getText());
            }
            groupIdTextField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                protected void textChanged(DocumentEvent e) {
                    basePackageTextField.setText(groupIdTextField.getText()
                            + "." + artifactIdTextField.getText());
                }
            });
            artifactIdTextField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                protected void textChanged(DocumentEvent e) {
                    basePackageTextField.setText(groupIdTextField.getText()
                            + "." + artifactIdTextField.getText());
                    if (moduleNameField != null) {
                        if (!syncInput.get()) {
                            artifactIdInput.set(true);
                            if (!moduleInput.get()) {
                                syncInput.set(true);
                                moduleNameField.setText(artifactIdTextField.getText());
                                syncInput.set(false);
                            }
                        }
                    }

                }
            });
        }

        if (moduleNameField != null) {
            moduleNameField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                protected void textChanged(DocumentEvent e) {
                    if (ProjectTypeEnum.MAVEN.getCode() == respDTO.getProjectType()) {
                        if (!syncInput.get()) {
                            moduleInput.set(true);
                            if (!artifactIdInput.get()) {
                                syncInput.set(true);
                                artifactIdTextField.setText(moduleNameField.getText());
                                syncInput.set(false);
                            }
                        }
                    }
                }
            });
        }

        return super.modifySettingsStep(settingsStep);
    }

    @Override
    public boolean validateModuleName(@NotNull String moduleName) throws ConfigurationException {
        ProjectTemplateRespDTO respDTO = projectTemplateService.detail(CrudSettings.currentGenerate().getPtId());
        try {
            if (respDTO.getProjectType() == ProjectTypeEnum.JAVA.getCode()) {
                String basePackage = javaView.getBasePackageTextField().getText();
                if (StringUtils.isBlank(basePackage)) {
                    throw new BizException("basePackage不能为空");
                }
                CrudSettings.currentGenerate().setBasePackage(basePackage);
            } else if (respDTO.getProjectType() == ProjectTypeEnum.MAVEN.getCode()) {
                String groupId = mavenView.getGroupIdTextField().getText();
                String artifactId = mavenView.getArtifactIdTextField().getText();
                String version = mavenView.getVersionTextField().getText();
                String basePackage = mavenView.getBasePackageTextField().getText();
                if (StringUtils.isBlank(groupId)) {
                    throw new BizException("groupId不能为空");
                }
                if (StringUtils.isBlank(artifactId)) {
                    throw new BizException("artifactId不能为空");
                }
                if (StringUtils.isBlank(version)) {
                    throw new BizException("version不能为空");
                }
                if (StringUtils.isBlank(basePackage)) {
                    throw new BizException("basePackage不能为空");
                }
                Preconditions.checkArgument(com.github.mars05.crud.hub.common.util.StringUtils.isPackageName(basePackage), "basePackage格式错误");
                CrudSettings.currentGenerate().setGroupId(groupId);
                CrudSettings.currentGenerate().setArtifactId(artifactId);
                CrudSettings.currentGenerate().setVersion(version);
                CrudSettings.currentGenerate().setBasePackage(basePackage);
            }
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), "表选择失败");
        }
        return super.validateModuleName(moduleName);
    }

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        if (this.myJdk != null) {
            rootModel.setSdk(this.myJdk);
        } else {
            rootModel.inheritSdk();
        }
        Project project = rootModel.getProject();
        VirtualFile root = createAndGetContentEntry();
        rootModel.addContentEntry(root);

        CrudSettings.currentGenerate().setProjectName(project.getName());
        CrudUtils.runWhenInitialized(project, () -> new WriteCommandAction<VirtualFile>(project) {
            @Override
            protected void run(@NotNull Result<VirtualFile> result) throws Throwable {
                ProjectGenerateReqDTO projectGenerateReqDTO = BeanUtils.convertBean(CrudSettings.currentGenerate(), ProjectGenerateReqDTO.class);
                ProjectRespDTO respDTO = projectService.generateProject(projectGenerateReqDTO);
                projectService.processProjectToDisk(respDTO, root.getParent().getCanonicalPath());
                VirtualFileManager.getInstance().asyncRefresh(() -> {
                });
            }
        }.execute());
    }

    private void initProject(Project project) throws Exception {
        //解决依赖
        try {
            MavenProjectsManager.getInstance(project).forceUpdateAllProjectsOrFindAllAvailablePomFiles();
        } catch (Exception ignored) {
        }
        //优化生成的所有Java类
        CrudUtils.doOptimize(project);
    }

    private VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }
}

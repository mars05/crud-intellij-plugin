package com.github.mars05.crud.intellij.plugin.wizard;

import com.github.mars05.crud.intellij.plugin.CrudBundle;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.github.mars05.crud.intellij.plugin.step.CrudConnStep;
import com.github.mars05.crud.intellij.plugin.step.CrudDbStep;
import com.github.mars05.crud.intellij.plugin.step.CrudTableStep;
import com.github.mars05.crud.intellij.plugin.ui.CrudConnView;
import com.github.mars05.crud.intellij.plugin.ui.CrudDbView;
import com.github.mars05.crud.intellij.plugin.ui.CrudTableView;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.github.mars05.crud.intellij.plugin.util.PsiFileUtils;
import com.github.mars05.crud.intellij.plugin.util.Selection;
import com.github.mars05.crud.intellij.plugin.util.SelectionContext;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.*;
import java.io.File;

/**
 * @author xiaoyu
 */
public class SpringBootModuleBuilder extends ModuleBuilder {

    public SpringBootModuleBuilder() {
    }

    @Override
    public Icon getNodeIcon() {
        return CrudIcons.SPRING_BOOT;
    }

    @Override
    public String getPresentableName() {
        return CrudBundle.message("wizard.spring.boot.name");
    }


    @Override
    public String getDescription() {
        return CrudBundle.message("wizard.spring.boot.description");
    }

    @Nullable
    @Override
    public String getBuilderId() {
        return getClass().getName();
    }

    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        final JTextField moduleNameField = settingsStep.getModuleNameField();
        String artifactId = SelectionContext.getArtifactId();
        if (moduleNameField != null && !StringUtil.isEmptyOrSpaces(artifactId)) {
            moduleNameField.setText(StringUtil.sanitizeJavaIdentifier(artifactId));
        }
        return super.modifyProjectTypeStep(settingsStep);
    }

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        Selection selection = SelectionContext.copyToSelection();
        if (this.myJdk != null) {
            rootModel.setSdk(this.myJdk);
        } else {
            rootModel.inheritSdk();
        }
        SelectionContext.clearAllSet();
        Project project = rootModel.getProject();
        VirtualFile root = createAndGetContentEntry();
        rootModel.addContentEntry(root);
        CrudUtils.runWhenInitialized(project, () -> new WriteCommandAction<VirtualFile>(project) {
            @Override
            protected void run(@NotNull Result<VirtualFile> result) throws Throwable {
                initProject(project, selection);
            }
        }.execute());
    }

    private void initProject(Project project, Selection selection) throws Exception {
        initMavenStructure();
        //pom.xml生成
        PsiFileUtils.createPOMXML(project, createAndGetContentEntry(), selection);
        //swagger生成
        PsiFileUtils.createSwagger(project, createPackageDir(selection.getPackage() + ".config"), selection);
        //Application类生成
        PsiFileUtils.createApplicationJava(project, createPackageDir(selection.getPackage()), selection);
        //application.yml配置生成
        PsiFileUtils.createApplicationYml(project, createResourceDir("/"), selection);

        selection.setModelPackage(selection.getPackage() + ".model");
        selection.setDaoPackage(selection.getPackage() + ".dao");
        if (selection.getOrmType() == SelectionContext.MYBATIS) {
            selection.setMapperDir(getContentEntryPath() + "/src/main/resources/mapper");
        }
        selection.setServicePackage(selection.getPackage() + ".service");
        selection.setControllerPackage(selection.getPackage() + ".controller");

        PsiFileUtils.createCrud(project, selection, getContentEntryPath());
        //解决依赖
        MavenProjectsManager.getInstance(project).forceUpdateAllProjectsOrFindAllAvailablePomFiles();
        //优化生成的所有Java类
        CrudUtils.doOptimize(project);
    }

    private VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    private void initMavenStructure() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath()) + "/";
        new File(path + "src/main/java").mkdirs();
        new File(path + "src/main/resources").mkdirs();
        new File(path + "src/test/java").mkdirs();
        new File(path + "src/test/resources").mkdirs();
    }

    private VirtualFile createPackageDir(String packageName) {
        packageName = "src/main/java/" + packageName;
        String path = FileUtil.toSystemIndependentName(getContentEntryPath() + "/" + StringUtil.replace(packageName, ".", "/"));
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    private VirtualFile createResourceDir(String childDir) {
        String resourceDir = "src/main/resources" + childDir;
        String path = FileUtil.toSystemIndependentName(getContentEntryPath() + "/" + resourceDir);
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    @Override
    public ModuleType getModuleType() {
        return StdModuleTypes.JAVA;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        SelectionContext.clearAllSet();
        CrudTableStep tableStep = new CrudTableStep(new CrudTableView());
        CrudDbStep dbStep = new CrudDbStep(new CrudDbView(), tableStep);
        CrudConnStep connStep = new CrudConnStep(new CrudConnView(), dbStep);
        return new ModuleWizardStep[]{
                connStep,
                dbStep,
                tableStep,
                new CrudProjectInfoStep()
        };
    }
}

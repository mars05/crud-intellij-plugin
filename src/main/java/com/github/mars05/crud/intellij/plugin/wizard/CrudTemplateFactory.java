package com.github.mars05.crud.intellij.plugin.wizard;

import com.github.mars05.crud.intellij.plugin.CrudBundle;
import com.github.mars05.crud.intellij.plugin.icon.CrudIcons;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import com.intellij.platform.templates.BuilderBasedTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author xiaoyu
 */
public class CrudTemplateFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{CrudBundle.message("wizard.group.name")};
    }

    @Override
    public Icon getGroupIcon(String group) {
        return CrudIcons.CRUD_ICON;
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(@Nullable String group, WizardContext context) {
        return new ProjectTemplate[]{
                new BuilderBasedTemplate(new SpringBootModuleBuilder()),
        };
    }
}

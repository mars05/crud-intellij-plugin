package com.github.mars05.crud.intellij.plugin.action;

import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.step.*;
import com.github.mars05.crud.intellij.plugin.ui.CrudDbView;
import com.github.mars05.crud.intellij.plugin.ui.CrudTableView;
import com.intellij.ide.util.newProjectWizard.AbstractProjectWizard;
import com.intellij.ide.util.newProjectWizard.StepSequence;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * @author xiaoyu
 */
public class CrudActionDialog extends AbstractProjectWizard {
    private final StepSequence mySequence = new StepSequence();
    private Project myProject;
    private Module myModule;

    public CrudActionDialog(Project project, Module module) {
        super("代码生成", project, "");
        myProject = project;
        myModule = module;
        ModuleWizardStep[] wizardSteps = createWizardSteps();
        for (ModuleWizardStep wizardStep : wizardSteps) {
            mySequence.addCommonStep(wizardStep);
        }
        for (ModuleWizardStep step : mySequence.getAllSteps()) {
            addStep(step);
        }
        init();
    }

    @Override
    public StepSequence getSequence() {
        return mySequence;
    }

    public ModuleWizardStep[] createWizardSteps() {
        CrudTableStep tableStep = new CrudTableStep(new CrudTableView());
        CrudDbStep dbStep = new CrudDbStep(new CrudDbView(), tableStep);

        CrudSettings.setCodeGenerate(new CodeGenerateReqDTO());
        return new ModuleWizardStep[]{
                new MyTemplateStep(),
                new CodeStep(),
                new DataSelectStep(),
                new DdlStep(),
                new CrudConnStep(),
                dbStep,
                tableStep,
                new CrudDirSelectInfoStep(myProject, myModule)
        };
    }

    @Nullable
    @Override
    protected String getHelpID() {
        return CrudActionDialog.class.getName();
    }
}


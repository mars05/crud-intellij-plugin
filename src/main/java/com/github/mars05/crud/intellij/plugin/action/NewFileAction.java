package com.github.mars05.crud.intellij.plugin.action;

import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.FileRespDTO;
import com.github.mars05.crud.intellij.plugin.service.ProjectService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author xiaoyu
 */
public class NewFileAction extends AnAction {
    private ProjectService projectService = new ProjectService();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(DataKeys.VIRTUAL_FILE);
        if (!virtualFile.isDirectory()) {
            virtualFile = virtualFile.getParent();
        }
        Module module = ModuleUtil.findModuleForFile(virtualFile, project);

        String moduleRootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();
        String actionDir = virtualFile.getPath();

        String str = StringUtils.substringAfter(actionDir, moduleRootPath + "/src/main/java/");
        String basePackage = StringUtils.replace(str, "/", ".");

        CrudSettings.getGenerate(project.getName()).setCodeGenerate(true);

        CrudActionDialog dialog = new CrudActionDialog(project, module);
        if (!dialog.showAndGet()) {
            return;
        }
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                try {
                    CodeGenerateReqDTO reqDTO = BeanUtils.convertBean(CrudSettings.currentGenerate(), CodeGenerateReqDTO.class);
                    List<FileRespDTO> fileRespDTOS = projectService.generateCode(reqDTO);
                    projectService.processCodeToDisk(moduleRootPath, fileRespDTOS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //优化生成的所有Java类
                CrudUtils.doOptimize(project);
            }
        }.execute());
    }
}

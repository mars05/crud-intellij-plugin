package com.github.mars05.crud.intellij.plugin.action;

import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.github.mars05.crud.intellij.plugin.util.PsiFileUtils;
import com.github.mars05.crud.intellij.plugin.util.Selection;
import com.github.mars05.crud.intellij.plugin.util.SelectionContext;
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
import org.jetbrains.idea.maven.project.MavenProjectsManager;

/**
 * @author xiaoyu
 */
public class NewFileAction extends AnAction {
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
        SelectionContext.clearAllSet();

        SelectionContext.setPackage(basePackage);
        if (StringUtils.isNotBlank(basePackage)) {
            basePackage += ".";
        }
        SelectionContext.setControllerPackage(basePackage + "controller");
        SelectionContext.setServicePackage(basePackage + "service");
        SelectionContext.setDaoPackage(basePackage + "dao");
        SelectionContext.setModelPackage(basePackage + "model");
        SelectionContext.setMapperDir(moduleRootPath + "/src/main/resources/mapper");

        CrudActionDialog dialog = new CrudActionDialog(project, module);
        if (!dialog.showAndGet()) {
            return;
        }
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                Selection selection = SelectionContext.copyToSelection();
                SelectionContext.clearAllSet();
                try {
                    PsiFileUtils.createCrud(project, selection, moduleRootPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //解决依赖
                MavenProjectsManager.getInstance(project).forceUpdateAllProjectsOrFindAllAvailablePomFiles();
                //优化生成的所有Java类
                CrudUtils.doOptimize(project);
            }
        }.execute());
    }
}

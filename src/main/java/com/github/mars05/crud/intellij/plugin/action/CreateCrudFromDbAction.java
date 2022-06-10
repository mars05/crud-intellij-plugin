package com.github.mars05.crud.intellij.plugin.action;

import com.github.mars05.crud.hub.common.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.hub.common.dto.FileRespDTO;
import com.github.mars05.crud.hub.common.service.ProjectService;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudActionDialog;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author xiaoyu
 * @see com.intellij.ide.actions.CreateClassAction
 */
public class CreateCrudFromDbAction extends AnAction {
    private static final String NOTIFICATION_GROUP = "Crud Code Generation";
    private final ProjectService projectService = CrudUtils.getBean(ProjectService.class);

    @Override
    public void update(AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Presentation presentation = e.getPresentation();

        final boolean enabled = isAvailable(dataContext);

        presentation.setEnabled(enabled);
    }

    protected boolean isAvailable(DataContext dataContext) {
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        return project != null && view != null && view.getDirectories().length != 0;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(DataKeys.VIRTUAL_FILE);
        if (!virtualFile.isDirectory()) {
            virtualFile = virtualFile.getParent();
        }
        String projectPath = "";
        String basePackage = "";
        Module module = ModuleUtil.findModuleForFile(virtualFile, project);
        if (module != null) {
            String moduleRootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();
            String actionDir = virtualFile.getPath();

            projectPath = moduleRootPath;
            String str = StringUtils.substringAfter(actionDir, moduleRootPath + "/src/main/java/");
            basePackage = StringUtils.replace(str, "/", ".");
        } else {
            projectPath = project.getPresentableUrl();
        }

        GenerateDTO generateDTO = CrudSettings.getGenerate(project.getName());
        generateDTO.setCodeGenerate(true);
        if (StringUtils.isNotBlank(basePackage) && StringUtils.isBlank(CrudSettings.currentGenerate().getBasePackage())) {
            CrudSettings.currentGenerate().setBasePackage(basePackage);
        }
        if (StringUtils.isNotBlank(projectPath) && StringUtils.isBlank(CrudSettings.currentGenerate().getProjectPath())) {
            CrudSettings.currentGenerate().setProjectPath(projectPath);
        }

        generateDTO.setDsId(null);
        generateDTO.setDatabase(null);
        generateDTO.setSchema(null);
        generateDTO.setTables(null);
        generateDTO.setDdl(null);
        generateDTO.setModelTables(null);
        generateDTO.setDdlSelected(false);

        CrudActionDialog dialog = new CrudActionDialog(project, module);
        if (!dialog.showAndGet()) {
            return;
        }
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                CrudUtils.runInBackground(new Task.Backgroundable(project, "代码生成中...", true) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        try {
                            CodeGenerateReqDTO reqDTO = BeanUtils.convertBean(CrudSettings.currentGenerate(), CodeGenerateReqDTO.class);
                            CrudSettings.saveGenerate(project.getName());
                            List<FileRespDTO> fileRespDTOS = projectService.generateCode(reqDTO);
                            List<FileRespDTO> successList = projectService.processCodeToDisk(reqDTO.getProjectPath(), fileRespDTOS);

                            Notifications.Bus.notify(new Notification(NOTIFICATION_GROUP, "代码生成完成", "生成数量: " + successList.size() + "\n失败数量: "
                                    + (fileRespDTOS.size() - successList.size()) + "\n项目路径: " + reqDTO.getProjectPath(), NotificationType.INFORMATION), project);
                            //优化生成的所有Java类
                            CrudUtils.doOptimize(project);
                            VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                        } catch (Exception ex) {
                            Notifications.Bus.notify(new Notification(NOTIFICATION_GROUP, "代码生成失败", ex.getMessage(), NotificationType.INFORMATION), project);
                        }
                    }
                });
            }
        }.execute());
    }
}

package com.github.mars05.crud.intellij.plugin.action;

import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.FileRespDTO;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.model.Column;
import com.github.mars05.crud.intellij.plugin.model.Table;
import com.github.mars05.crud.intellij.plugin.service.ProjectService;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.ui.CrudActionDialog;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.github.mars05.crud.intellij.plugin.util.JavaTypeUtils;
import com.google.common.base.CaseFormat;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
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
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoyu
 * @see com.intellij.ide.actions.CreateClassAction
 */
public class CreateCrudFromModelAction extends AnAction {
    private static final String NOTIFICATION_GROUP = "Crud Code Generation";
    private final ProjectService projectService = new ProjectService();

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        final Presentation presentation = e.getPresentation();
        VirtualFile[] virtualFiles = e.getData(DataKeys.VIRTUAL_FILE_ARRAY);
        if (virtualFiles == null || virtualFiles.length == 0) {
            presentation.setEnabled(false);
        } else {
            for (VirtualFile virtualFile : virtualFiles) {
                if (virtualFile.isDirectory() || !(PsiManager.getInstance(project).findFile(virtualFile) instanceof PsiJavaFile)) {
                    presentation.setEnabled(false);
                    return;
                }
            }
            presentation.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] virtualFiles = e.getData(DataKeys.VIRTUAL_FILE_ARRAY);

        String projectPath = "";
        String basePackage = "";
        Module module = ModuleUtil.findModuleForFile(virtualFiles[0], project);
        if (module != null) {
            String moduleRootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();
            String actionDir = virtualFiles[0].getPath();

            projectPath = moduleRootPath;
            String str = StringUtils.substringAfter(actionDir, moduleRootPath + "/src/main/java/");
            basePackage = StringUtils.replace(str, "/", ".");
        } else {
            projectPath = project.getPresentableUrl();
        }

        GenerateDTO generateDTO = CrudSettings.getGenerate(project.getName());
        generateDTO.setCodeGenerate(true);

        try {
            List<Table> tables = new ArrayList<>();
            for (VirtualFile virtualFile : virtualFiles) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                if (!(psiFile instanceof PsiJavaFile)) {
                    throw new BizException("实体类错误: " + virtualFile.getName());
                }
                TableVisitor tableVisitor = new TableVisitor();
                psiFile.accept(tableVisitor);

                Table table = tableVisitor.getTable();
                if (tables.stream().anyMatch(table1 -> table1.getTableName().equals(table.getTableName()))) {
                    throw new BizException("[" + table.getTableName() + "]表名重复");
                }

                tables.add(table);
            }
            CrudSettings.currentGenerate().setModelTables(tables);
        } catch (Exception exception) {
            Messages.showErrorDialog(exception.getMessage(), "错误");
            return;
        }

        generateDTO.setDsId(null);
        generateDTO.setDatabase(null);
        generateDTO.setSchema(null);
        generateDTO.setTables(null);
        generateDTO.setDdl(null);
        generateDTO.setDdlSelected(false);

        if (StringUtils.isNotBlank(basePackage) && StringUtils.isBlank(CrudSettings.currentGenerate().getBasePackage())) {
            CrudSettings.currentGenerate().setBasePackage(basePackage);
        }
        if (StringUtils.isNotBlank(projectPath) && StringUtils.isBlank(CrudSettings.currentGenerate().getProjectPath())) {
            CrudSettings.currentGenerate().setProjectPath(projectPath);
        }

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

    public static class TableVisitor extends JavaRecursiveElementVisitor {
        private Table table;

        public Table getTable() {
            return table;
        }

        @Override
        public void visitClass(PsiClass aClass) {
            if (table != null || aClass == null || aClass.getName() == null) {
                return;
            }
            Table t = new Table();
            t.setTableName(getTableName(aClass));
            t.setRemarks(getRemarks(aClass.getDocComment()));
            t.setColumns(getColumns(aClass.getFields()));
//            boolean key = false;
//            for (Column column : t.getColumns()) {
//                if (column.getPrimaryKey() != null && column.getPrimaryKey()) {
//                    key = true;
//                    break;
//                }
//            }
//            if (!key) {
//                throw new BizException("[" + aClass.getName() + "]实体类中没有找到主键");
//            }
            this.table = t;
        }

        private String getRemarks(PsiDocComment comment) {
            if (comment != null) {
                for (PsiElement descriptionElement : comment.getDescriptionElements()) {
                    if (descriptionElement instanceof PsiDocToken) {
                        return descriptionElement.getText().trim();
                    }
                }
            }
            return "";
        }

        private String getTableName(PsiClass aClass) {
            for (PsiAnnotation annotation : aClass.getAnnotations()) {
                if (annotation.getQualifiedName().endsWith("TableName")) {
                    PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
                    if (value == null) {
                        break;
                    }
                    return value.getText().replaceAll("(^\"|\"$)", "");
                } else if (annotation.getQualifiedName().endsWith("Table")) {
                    PsiAnnotationMemberValue value = annotation.findAttributeValue("name");
                    if (value == null) {
                        break;
                    }
                    return value.getText().replaceAll("(^\"|\"$)", "");
                }
            }
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, aClass.getName());
        }

        private List<Column> getColumns(PsiField[] fields) {
            List<Column> columns = new ArrayList<>();
            for (PsiField field : fields) {
                if (null == field.getName()) {
                    continue;
                }
                Integer ofType = null;
                try {
                    ofType = JavaTypeUtils.ofType(Class.forName(field.getType().getCanonicalText()));
                } catch (ClassNotFoundException ignored) {
                }
                if (ofType == null) {
                    continue;
                }
                Column column = new Column();
                column.setColumnName(getColumnName(field));
                column.setRemarks(getRemarks(field.getDocComment()));
                column.setType(ofType);
                Boolean primaryKey = getPrimaryKey(field);
                if (primaryKey) {
                    columns.forEach(c -> c.setPrimaryKey(false));
                }
                column.setPrimaryKey(primaryKey);
                columns.add(column);
            }
            return columns;
        }

        private Boolean getPrimaryKey(PsiField field) {
            for (PsiAnnotation annotation : field.getAnnotations()) {
                if (annotation.getQualifiedName().endsWith("TableId")) {
                    return true;
                } else if (annotation.getQualifiedName().endsWith("Id")) {
                    return true;
                }
            }
            return "id".equals(getColumnName(field));
        }

        private String getColumnName(PsiField field) {
            for (PsiAnnotation annotation : field.getAnnotations()) {
                if (annotation.getQualifiedName().endsWith("TableField")) {
                    PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
                    if (value == null) {
                        break;
                    }
                    return value.getText().replaceAll("(^\"|\"$)", "");
                } else if (annotation.getQualifiedName().endsWith("Column")) {
                    PsiAnnotationMemberValue value = annotation.findAttributeValue("name");
                    if (value == null) {
                        break;
                    }
                    return value.getText().replaceAll("(^\"|\"$)", "");
                }
            }
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
    }

}

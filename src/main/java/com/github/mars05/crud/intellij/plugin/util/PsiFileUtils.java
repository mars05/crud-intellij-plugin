package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.model.*;
import com.github.mars05.crud.intellij.plugin.setting.CrudSettings;
import com.github.mars05.crud.intellij.plugin.setting.SelectionSaveInfo;
import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyu
 */
public class PsiFileUtils {
    private static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates");

    public static void createPOMXML(Project project, VirtualFile root, Selection selection) {
        try {
            VirtualFile virtualFile = root.createChildData(project, "pom.xml");
            StringWriter sw = new StringWriter();
            Template template = freemarker.getTemplate("pom.ftl");
            template.process(selection, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createSwagger(Project project, VirtualFile root, Selection selection) {
        try {
            VirtualFile virtualFile = root.createChildData(project, "Swagger2Config.java");
            StringWriter sw = new StringWriter();
            Template template = freemarker.getTemplate("Swagger2Config.ftl");
            template.process(selection, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createApplicationJava(Project project, VirtualFile root, Selection selection) {
        try {
            VirtualFile virtualFile = root.createChildData(project, "Application.java");
            StringWriter sw = new StringWriter();
            Template template = freemarker.getTemplate("Application.java.ftl");
            template.process(selection, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createApplicationYml(Project project, VirtualFile root, Selection selection) {
        try {
            VirtualFile virtualFile = root.createChildData(project, "application.yml");
            StringWriter sw = new StringWriter();
            Template template = freemarker.getTemplate("application.yml.ftl");
            template.process(selection, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createMapper(Project project, VirtualFile packageDir, Dao dao) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, dao.getModel().getSimpleName() + "Mapper.xml");
            StringWriter sw = new StringWriter();
            Template template = freemarker.getTemplate("mybatis/mapper.ftl");
            template.process(dao, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createModel(Project project, VirtualFile packageDir, Model model) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, model.getSimpleName() + ".java");
            StringWriter sw = new StringWriter();
            String templateName;
            if (model.getOrmType() == OrmType.JPA) {
                templateName = "jpa/model.ftl";
            } else if (model.getOrmType() == OrmType.MYBATIS) {
                templateName = "mybatis/model.ftl";
            } else {
                templateName = "mybatisplus/model.ftl";
            }
            Template template = freemarker.getTemplate(templateName);
            template.process(model, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));
            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDao(Project project, VirtualFile packageDir, Dao dao) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, dao.getSimpleName() + ".java");
            StringWriter sw = new StringWriter();
            String templateName;
            if (dao.getOrmType() == OrmType.JPA) {
                templateName = "jpa/dao.ftl";
            } else if (dao.getOrmType() == OrmType.MYBATIS) {
                templateName = "mybatis/dao.ftl";
            } else {
                templateName = "mybatisplus/dao.ftl";
            }
            Template template = freemarker.getTemplate(templateName);
            template.process(dao, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));

            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createService(Project project, VirtualFile packageDir, Service service) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, service.getSimpleName() + ".java");
            StringWriter sw = new StringWriter();
            String templateName;
            if (service.getOrmType() == OrmType.JPA) {
                templateName = "jpa/service.ftl";
            } else if (service.getOrmType() == OrmType.MYBATIS) {
                templateName = "mybatis/service.ftl";
            } else {
                templateName = "mybatisplus/service.ftl";
            }
            Template template = freemarker.getTemplate(templateName);
            template.process(service, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));

            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createServiceImpl(Project project, VirtualFile packageDir, Service service) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, service.getSimpleName() + "Impl.java");
            StringWriter sw = new StringWriter();
            String templateName;
            if (service.getOrmType() == OrmType.JPA) {
                templateName = "jpa/service_impl.ftl";
            } else if (service.getOrmType() == OrmType.MYBATIS) {
                templateName = "mybatis/service_impl.ftl";
            } else {
                templateName = "mybatisplus/service_impl.ftl";
            }
            Template template = freemarker.getTemplate(templateName);
            template.process(service, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));

            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createController(Project project, VirtualFile packageDir, Controller controller) {
        try {
            VirtualFile virtualFile = packageDir.createChildData(project, controller.getSimpleName() + ".java");
            StringWriter sw = new StringWriter();
            String templateName;
            if (controller.getOrmType() == OrmType.JPA) {
                templateName = "jpa/controller.ftl";
            } else if (controller.getOrmType() == OrmType.MYBATIS) {
                templateName = "mybatis/controller.ftl";
            } else {
                templateName = "mybatisplus/controller.ftl";
            }
            Template template = freemarker.getTemplate(templateName);
            template.process(controller, sw);
            virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));

            CrudUtils.addWaitOptimizeFile(virtualFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static VirtualFile createPackageDir(String packageName, String moduleRootPath) {
        packageName = "src/main/java/" + packageName;
        String path = FileUtil.toSystemIndependentName(moduleRootPath + "/" + StringUtil.replace(packageName, ".", "/"));
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    public static void createCrud(Project project, Selection selection, String moduleRootPath) {
        createCrud(project, selection, moduleRootPath, false);
    }

    public static void createCrud(Project project, Selection selection, String moduleRootPath, boolean isSaveSelection) {
        if (isSaveSelection) {
            SelectionSaveInfo selectionSaveInfo = new SelectionSaveInfo();

            selectionSaveInfo.setOrmType(selection.getOrmType());

            selectionSaveInfo.setModelPackage(selection.getModelPackage());
            selectionSaveInfo.setDaoPackage(selection.getDaoPackage());
            selectionSaveInfo.setServicePackage(selection.getServicePackage());
            selectionSaveInfo.setControllerPackage(selection.getControllerPackage());
            selectionSaveInfo.setMapperDir(selection.getMapperDir());

            selectionSaveInfo.setModelSelected(selection.isModelSelected());
            selectionSaveInfo.setDaoSelected(selection.isDaoSelected());
            selectionSaveInfo.setServiceSelected(selection.isServiceSelected());
            selectionSaveInfo.setControllerSelected(selection.isControllerSelected());

            Map<String, SelectionSaveInfo> selectionSaveInfoMap = CrudSettings.getSelectionSaveInfoMap();
            selectionSaveInfoMap.put(project.getName(), selectionSaveInfo);
        }

        List<Table> tables = selection.getTables();
        for (Table table : tables) {
            //model生成
            List<Column> columns = table.getColumns();
            List<Field> fields = new ArrayList<>();
            for (Column column : columns) {
                Field field = new Field(column.getComment(), JavaTypeUtils.convertType(column.getType()), column.getName());
                field.setId(column.isId());
                fields.add(field);
            }
            String modelPackage = selection.getModelPackage();
            if (modelPackage == null||(isSaveSelection&&!selection.isModelSelected())) {
                return;
            }
            VirtualFile modelPackageDir = createPackageDir(modelPackage, moduleRootPath);
            if (!StringUtils.isBlank(modelPackage)) {
                modelPackage += ".";
            }
            Model model = new Model(table.getComment(), modelPackage + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()), table.getName(), fields);
            model.setOrmType(selection.getOrmType());
            PsiFileUtils.createModel(project, modelPackageDir, model);
            //dao生成
            String daoPackage = selection.getDaoPackage();
            if (daoPackage == null||(isSaveSelection&&!selection.isDaoSelected())) {
                continue;
            }
            VirtualFile daoPackageDir = createPackageDir(daoPackage, moduleRootPath);
            if (!StringUtils.isBlank(daoPackage)) {
                daoPackage += ".";
            }
            Dao dao = new Dao(table.getComment(), daoPackage + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + "DAO", model);
            PsiFileUtils.createDao(project, daoPackageDir, dao);
            //mybatis生成mapper.xml
            if (selection.getOrmType() == OrmType.MYBATIS) {
                String mapperDir = selection.getMapperDir();
                if (StringUtils.isNotBlank(mapperDir)) {
                    String path = FileUtil.toSystemIndependentName(mapperDir);
                    new File(path).mkdirs();
                    VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
                    PsiFileUtils.createMapper(project, virtualFile, dao);
                }
            }
            //service生成
            //接口
            String servicePackage = selection.getServicePackage();
            if (servicePackage == null||(isSaveSelection&&!selection.isServiceSelected())) {
                continue;
            }
            VirtualFile servicePackageDir = createPackageDir(servicePackage, moduleRootPath);
            if (!StringUtils.isBlank(servicePackage)) {
                servicePackage += ".";
            }
            Service service = new Service(table.getComment(), servicePackage + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + "Service", dao);
            PsiFileUtils.createService(project, servicePackageDir, service);
            //实现
            String serviceImplPackage = servicePackage + "impl";
            VirtualFile serviceImplPackageDir = createPackageDir(serviceImplPackage, moduleRootPath);
            service.getImports().add(service.getDao().getName());
            service.getImports().add(service.getName());
            PsiFileUtils.createServiceImpl(project, serviceImplPackageDir, service);
            //controller生成
            String controllerPackage = selection.getControllerPackage();
            if (controllerPackage == null||(isSaveSelection&&!selection.isControllerSelected())) {
                continue;
            }
            VirtualFile controllerPackageDir = createPackageDir(controllerPackage, moduleRootPath);
            if (!StringUtils.isBlank(controllerPackage)) {
                controllerPackage += ".";
            }
            Controller controller = new Controller(table.getComment(), controllerPackage + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + "Controller", service);
            PsiFileUtils.createController(project, controllerPackageDir, controller);
        }
    }
}

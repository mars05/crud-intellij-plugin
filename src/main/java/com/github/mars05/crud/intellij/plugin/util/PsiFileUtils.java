package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.model.Column;
import com.github.mars05.crud.intellij.plugin.model.Controller;
import com.github.mars05.crud.intellij.plugin.model.Dao;
import com.github.mars05.crud.intellij.plugin.model.Field;
import com.github.mars05.crud.intellij.plugin.model.Model;
import com.github.mars05.crud.intellij.plugin.model.Service;
import com.github.mars05.crud.intellij.plugin.model.Table;
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
			Template template = freemarker.getTemplate("mapper.ftl");
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
			if (model.getOrmType() == SelectionContext.MYBATIS) {
				templateName = "model_mybatis.ftl";
			} else {
				templateName = "model_jpa.ftl";
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
			if (dao.getOrmType() == SelectionContext.MYBATIS) {
				templateName = "dao_mybatis.ftl";
			} else {
				templateName = "dao_jpa.ftl";
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
			Template template = freemarker.getTemplate("service.ftl");
			template.process(service, sw);
			virtualFile.setBinaryContent(sw.toString().getBytes(CrudUtils.DEFAULT_CHARSET));

			CrudUtils.addWaitOptimizeFile(virtualFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createServiceImplJpa(Project project, VirtualFile packageDir, Service service) {
		try {
			VirtualFile virtualFile = packageDir.createChildData(project, service.getSimpleName() + "Impl.java");
			StringWriter sw = new StringWriter();
			Template template = freemarker.getTemplate("service_impl.ftl");
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
			Template template = freemarker.getTemplate("controller.ftl");
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
			if (modelPackage == null) {
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
			if (daoPackage == null) {
				continue;
			}
			VirtualFile daoPackageDir = createPackageDir(daoPackage, moduleRootPath);
			if (!StringUtils.isBlank(daoPackage)) {
				daoPackage += ".";
			}
			Dao dao = new Dao(table.getComment(), daoPackage + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + "DAO", model);
			PsiFileUtils.createDao(project, daoPackageDir, dao);
			//mybatis生成mapper.xml
			if (selection.getOrmType() == SelectionContext.MYBATIS) {
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
			if (servicePackage == null) {
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
			PsiFileUtils.createServiceImplJpa(project, serviceImplPackageDir, service);
			//controller生成
			String controllerPackage = selection.getControllerPackage();
			if (controllerPackage == null) {
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

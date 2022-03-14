package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.model.Table;

import java.util.List;

/**
 * @author xiaoyu
 */
public class SelectionContext {
    private static String projectType;
    private static int ormType;
    private static String db;
    private static List<Table> tables;
    private static String groupId;
    private static String artifactId;
    private static String version;
    private static String pkg;
    private static String modelPackage;
    private static String daoPackage;
    private static String servicePackage;
    private static String controllerPackage;
    private static String mapperDir;

    public static boolean modelSelected;
    public static boolean daoSelected;
    public static boolean serviceSelected;
    public static boolean controllerSelected;

    public static String getDb() {
        return db;
    }

    public static void setDb(String db) {
        SelectionContext.db = db;
    }

    public static List<Table> getTables() {
        return tables;
    }

    public static void setTables(List<Table> tables) {
        SelectionContext.tables = tables;
    }

    public static String getProjectType() {
        return projectType;
    }

    public static void setProjectType(String projectType) {
        SelectionContext.projectType = projectType;
    }


    public static String getGroupId() {
        return groupId;
    }

    public static void setGroupId(String groupId) {
        SelectionContext.groupId = groupId;
    }

    public static String getArtifactId() {
        return artifactId;
    }

    public static void setArtifactId(String artifactId) {
        SelectionContext.artifactId = artifactId;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SelectionContext.version = version;
    }

    public static String getPackage() {
        return pkg;
    }

    public static void setPackage(String pkg) {
        SelectionContext.pkg = pkg;
    }

    public static void clearAllSet() {
        projectType = null;
        db = null;
        tables = null;
        groupId = null;
        artifactId = null;
        version = null;
        pkg = null;
        daoPackage = null;
        servicePackage = null;
        controllerPackage = null;
        mapperDir = null;
        modelPackage = null;
        ormType = 0;
        modelSelected = false;
        daoSelected = false;
        serviceSelected = false;
        controllerSelected = false;
    }

    public static void clearSelection() {
        modelPackage = null;
        daoPackage = null;
        servicePackage = null;
        controllerPackage = null;
        mapperDir = null;
        modelSelected = false;
        daoSelected = false;
        serviceSelected = false;
        controllerSelected = false;
    }

    public static Selection copyToSelection() {
        Selection selection = new Selection();
        selection.setProjectType(projectType);
        selection.setDb(db);
        selection.setTables(tables);
        selection.setGroupId(groupId);
        selection.setArtifactId(artifactId);
        selection.setVersion(version);
        selection.setPackage(pkg);
        selection.setDaoPackage(daoPackage);
        selection.setServicePackage(servicePackage);
        selection.setControllerPackage(controllerPackage);
        selection.setMapperDir(mapperDir);
        selection.setModelPackage(modelPackage);
        selection.setOrmType(ormType);
        selection.setModelSelected(modelSelected);
        selection.setDaoSelected(daoSelected);
        selection.setServiceSelected(serviceSelected);
        selection.setControllerSelected(controllerSelected);
        return selection;
    }

    public static void setOrmType(int ormType) {
        SelectionContext.ormType = ormType;
    }

    public static int getOrmType() {
        return ormType;
    }

    public static String getDaoPackage() {
        return daoPackage;
    }

    public static void setDaoPackage(String daoPackage) {
        SelectionContext.daoPackage = daoPackage;
    }

    public static String getServicePackage() {
        return servicePackage;
    }

    public static void setServicePackage(String servicePackage) {
        SelectionContext.servicePackage = servicePackage;
    }

    public static String getControllerPackage() {
        return controllerPackage;
    }

    public static void setControllerPackage(String controllerPackage) {
        SelectionContext.controllerPackage = controllerPackage;
    }

    public static String getMapperDir() {
        return mapperDir;
    }

    public static void setMapperDir(String mapperDir) {
        SelectionContext.mapperDir = mapperDir;
    }

    public static void setModelPackage(String modelPackage) {
        SelectionContext.modelPackage = modelPackage;
    }

    public static String getModelPackage() {
        return modelPackage;
    }

    public static boolean isModelSelected() {
        return modelSelected;
    }

    public static void setModelSelected(boolean modelSelected) {
        SelectionContext.modelSelected = modelSelected;
    }

    public static boolean isDaoSelected() {
        return daoSelected;
    }

    public static void setDaoSelected(boolean daoSelected) {
        SelectionContext.daoSelected = daoSelected;
    }

    public static boolean isServiceSelected() {
        return serviceSelected;
    }

    public static void setServiceSelected(boolean serviceSelected) {
        SelectionContext.serviceSelected = serviceSelected;
    }

    public static boolean isControllerSelected() {
        return controllerSelected;
    }

    public static void setControllerSelected(boolean controllerSelected) {
        SelectionContext.controllerSelected = controllerSelected;
    }
}

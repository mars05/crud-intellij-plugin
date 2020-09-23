package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.model.Table;
import com.github.mars05.crud.intellij.plugin.setting.Conn;

import java.util.List;

/**
 * @author xiaoyu
 */
public class Selection {
    private String projectType;
    private int ormType;
    private Conn conn;
    private String db;
    private List<Table> tables;
    private String groupId;
    private String artifactId;
    private String version;
    private String pkg;

    private String daoPackage;
    private String servicePackage;
    private String controllerPackage;
    private String mapperDir;
    private String modelPackage;

    public boolean modelSelected;
    public boolean daoSelected;
    public boolean serviceSelected;
    public boolean controllerSelected;

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Conn getConn() {
        return conn;
    }

    public void setConn(Conn conn) {
        this.conn = conn;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackage() {
        return pkg;
    }

    public void setPackage(String pkg) {
        this.pkg = pkg;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setOrmType(int ormType) {
        this.ormType = ormType;
    }

    public int getOrmType() {
        return ormType;
    }

    public boolean isModelSelected() {
        return modelSelected;
    }

    public void setModelSelected(boolean modelSelected) {
        this.modelSelected = modelSelected;
    }

    public boolean isDaoSelected() {
        return daoSelected;
    }

    public void setDaoSelected(boolean daoSelected) {
        this.daoSelected = daoSelected;
    }

    public boolean isServiceSelected() {
        return serviceSelected;
    }

    public void setServiceSelected(boolean serviceSelected) {
        this.serviceSelected = serviceSelected;
    }

    public boolean isControllerSelected() {
        return controllerSelected;
    }

    public void setControllerSelected(boolean controllerSelected) {
        this.controllerSelected = controllerSelected;
    }
}

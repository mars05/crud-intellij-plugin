package com.github.mars05.crud.intellij.plugin.setting;

/**
 * 包路径选择记录
 *
 * @author xiaoyu
 */
public class SelectionSaveInfo {
    private Integer ormType;
    private String modelPackage;
    private String daoPackage;
    private String servicePackage;
    private String controllerPackage;
    private String mapperDir;
    private Boolean modelSelected;
    private Boolean daoSelected;
    private Boolean serviceSelected;
    private Boolean controllerSelected;

    public Integer getOrmType() {
        return ormType;
    }

    public void setOrmType(Integer ormType) {
        this.ormType = ormType;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
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

    public Boolean getModelSelected() {
        return modelSelected;
    }

    public void setModelSelected(Boolean modelSelected) {
        this.modelSelected = modelSelected;
    }

    public Boolean getDaoSelected() {
        return daoSelected;
    }

    public void setDaoSelected(Boolean daoSelected) {
        this.daoSelected = daoSelected;
    }

    public Boolean getServiceSelected() {
        return serviceSelected;
    }

    public void setServiceSelected(Boolean serviceSelected) {
        this.serviceSelected = serviceSelected;
    }

    public Boolean getControllerSelected() {
        return controllerSelected;
    }

    public void setControllerSelected(Boolean controllerSelected) {
        this.controllerSelected = controllerSelected;
    }
}

package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author xiaoyu
 */
@State(name = "CrudSettings", storages = @Storage("crud-plugin.xml"))
public class CrudSettings implements PersistentStateComponent<CrudState> {
    private static final CrudSettings CRUD_SETTINGS = ServiceManager.getService(CrudSettings.class);
    private CrudState myState = new CrudState();

    private GenerateDTO generateDTO;

    @Nullable
    @Override
    public CrudState getState() {
        return myState;
    }

    @Override
    public void loadState(CrudState state) {
        myState = state;
    }

    public static List<DataSourceDO> getDataSources() {
        return CRUD_SETTINGS.myState.getDataSources();
    }

    public static List<ProjectTemplateDO> getProjectTemplates() {
        return CRUD_SETTINGS.myState.getProjectTemplates();
    }

    public static GenerateDTO getGenerate(String projectName) {
        CRUD_SETTINGS.generateDTO = CRUD_SETTINGS.myState.getGenerateInfoMap().getOrDefault(projectName, new GenerateDTO());
        return CRUD_SETTINGS.generateDTO;
    }

    public static GenerateDTO currentGenerate() {
        return CRUD_SETTINGS.generateDTO;
    }

    public static GenerateDTO newGenerate() {
        CRUD_SETTINGS.generateDTO = new GenerateDTO();
        return CRUD_SETTINGS.generateDTO;
    }

    public static void saveGenerate(String projectName) {
        if (CRUD_SETTINGS.generateDTO != null) {
            CRUD_SETTINGS.generateDTO.setDdl(null);
            CRUD_SETTINGS.generateDTO.setDsId(null);
            CRUD_SETTINGS.generateDTO.setDatabase(null);
            CRUD_SETTINGS.generateDTO.setSchema(null);
            CRUD_SETTINGS.generateDTO.setTables(null);
            CRUD_SETTINGS.myState.getGenerateInfoMap().put(projectName, CRUD_SETTINGS.generateDTO);
        }
        CRUD_SETTINGS.generateDTO = null;
    }

}

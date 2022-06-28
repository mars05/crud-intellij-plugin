package com.github.mars05.crud.intellij.plugin.setting;

import com.alibaba.fastjson.JSON;
import com.github.mars05.crud.hub.common.dto.ProjectTemplateDTO;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.io.StreamUtil;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 */
@State(name = "CrudSettings", storages = @Storage("crud-plugin-3.0.2.xml"))
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
        if (!CRUD_SETTINGS.myState.isInitialized()) {
            try {
                InputStream is = CRUD_SETTINGS.myState.getClass().getResourceAsStream("/templates/default_pts.json");
                String readText = StreamUtil.readText(is, Charset.defaultCharset());
                CRUD_SETTINGS.myState.getProjectTemplates().clear();
                List<ProjectTemplateDTO> list = JSON.parseArray(readText, ProjectTemplateDTO.class);
                CRUD_SETTINGS.myState.getProjectTemplates().addAll(list.stream().map(projectTemplateDTO -> {
                    ProjectTemplateDO projectTemplateDO = BeanUtils.convertBean(projectTemplateDTO, ProjectTemplateDO.class);
                    projectTemplateDO.setFileTemplates(JSON.toJSONString(projectTemplateDTO.getFileTemplateList()));
                    return projectTemplateDO;
                }).collect(Collectors.toList()));
            } catch (Exception ignored) {
            } finally {
                CRUD_SETTINGS.myState.setInitialized(true);
            }
        }
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
            CRUD_SETTINGS.generateDTO.setDataSource(null);
            CRUD_SETTINGS.generateDTO.setTables(null);
            CRUD_SETTINGS.myState.getGenerateInfoMap().put(projectName, CRUD_SETTINGS.generateDTO);
        }
        CRUD_SETTINGS.generateDTO = null;
    }

}

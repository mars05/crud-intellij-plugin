package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dto.CodeGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoyu
 */
@State(name = "CrudSettings", storages = @Storage("crud-plugin.xml"))
public class CrudSettings implements PersistentStateComponent<CrudState> {
    private static final CrudSettings CRUD_SETTINGS = ServiceManager.getService(CrudSettings.class);
    private CrudState myState = new CrudState();

    private static CodeGenerateReqDTO codeGenerate;
    private static ProjectGenerateReqDTO projectGenerate;

    @Nullable
    @Override
    public CrudState getState() {
        return myState;
    }

    @Override
    public void loadState(CrudState state) {
        myState = state;
    }

    public static List<Conn> getConns() {
        return CRUD_SETTINGS.myState.getConns();
    }

    public static List<DataSourceDO> getDataSources() {
        return CRUD_SETTINGS.myState.getDataSources();
    }

    public static List<ProjectTemplateDO> getProjectTemplates() {
        return CRUD_SETTINGS.myState.getProjectTemplates();
    }

    public static Map<String, SelectionSaveInfo> getSelectionSaveInfoMap() {
        return CRUD_SETTINGS.myState.getSelectionSaveInfoMap();
    }

    public static void setCodeGenerate(CodeGenerateReqDTO codeGenerate) {
        CrudSettings.codeGenerate = codeGenerate;
        CrudSettings.projectGenerate = null;
    }

    public static void setProjectGenerate(ProjectGenerateReqDTO projectGenerate) {
        CrudSettings.projectGenerate = projectGenerate;
        CrudSettings.codeGenerate = null;
    }

    public static <T> T getGenerate() {
        if (projectGenerate != null) {
            return (T) projectGenerate;
        } else if (codeGenerate != null) {
            return (T) codeGenerate;
        } else {
            throw new BizException("生成参数错误");
        }
    }
}

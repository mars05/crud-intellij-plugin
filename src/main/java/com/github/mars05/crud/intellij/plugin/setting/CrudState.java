package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dto.GenerateDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiaoyu
 */
@Data
public class CrudState {
    private List<DataSourceDO> dataSources = new CopyOnWriteArrayList<>();
    private List<ProjectTemplateDO> projectTemplates = new CopyOnWriteArrayList<>();

    private Map<String, SelectionSaveInfo> selectionSaveInfoMap = new ConcurrentHashMap<>();

    private Map<String, GenerateDTO> generateInfoMap = new ConcurrentHashMap<>();

}

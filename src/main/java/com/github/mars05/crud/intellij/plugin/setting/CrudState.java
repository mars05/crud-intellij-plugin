package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.FileTemplateDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiaoyu
 */
@Data
public class CrudState {
    private List<DataSourceDO> dataSources = new CopyOnWriteArrayList<>();
    private List<ProjectTemplateDO> projectTemplates = new CopyOnWriteArrayList<>();
    private List<FileTemplateDO> fileTemplates = new CopyOnWriteArrayList<>();

    private List<Conn> conns = new ArrayList<>();

    private Map<String, SelectionSaveInfo> selectionSaveInfoMap = new HashMap<>();

}

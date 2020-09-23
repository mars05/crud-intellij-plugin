package com.github.mars05.crud.intellij.plugin.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyu
 */
public class CrudState {

    private List<Conn> conns = new ArrayList<>();

    public List<Conn> getConns() {
        return conns;
    }

    public void setConns(List<Conn> conns) {
        this.conns = conns;
    }

    private Map<String, SelectionSaveInfo> selectionSaveInfoMap = new HashMap<>();

    public void setSelectionSaveInfoMap(Map<String, SelectionSaveInfo> selectionSaveInfoMap) {
        this.selectionSaveInfoMap = selectionSaveInfoMap;
    }

    public Map<String, SelectionSaveInfo> getSelectionSaveInfoMap() {
        return selectionSaveInfoMap;
    }
}

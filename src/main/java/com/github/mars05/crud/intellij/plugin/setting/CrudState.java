package com.github.mars05.crud.intellij.plugin.setting;

import java.util.ArrayList;
import java.util.List;

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
}

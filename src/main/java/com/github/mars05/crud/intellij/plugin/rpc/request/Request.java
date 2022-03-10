package com.github.mars05.crud.intellij.plugin.rpc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.mars05.crud.intellij.plugin.rpc.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyu
 */
public abstract class Request<T extends Response> {
    @JSONField(serialize = false)
    public String getMethod() {
        return "POST";
    }

    @JSONField(serialize = false)
    public String getBody() {
        return "{}";
    }

    @JSONField(serialize = false)
    public Map<String, String> getQuery() {
        return new HashMap<>();
    }

    @JSONField(serialize = false)
    public abstract String getPah();

    @JSONField(serialize = false)
    public abstract Class<T> getResponseClass();

}
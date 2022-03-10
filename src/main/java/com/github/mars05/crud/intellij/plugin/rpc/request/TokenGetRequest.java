package com.github.mars05.crud.intellij.plugin.rpc.request;

import com.github.mars05.crud.intellij.plugin.rpc.response.ProjectTemplateResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TokenGetRequest extends Request<ProjectTemplateResponse> {
    private String accessToken;

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Map<String, String> getQuery() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }

    @Override
    public String getPah() {
        return "/api/crud-hub/plugin/getTemplate";
    }

    @Override
    public Class<ProjectTemplateResponse> getResponseClass() {
        return ProjectTemplateResponse.class;
    }
}
package com.github.mars05.crud.intellij.plugin.rpc.response;

import com.alibaba.fastjson.JSON;
import com.github.mars05.crud.hub.common.dto.ProjectTemplateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 市场模板
 *
 * @author xioayu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectTemplateResponse extends Response {

    private ProjectTemplateDTO projectTemplate;

    @Override
    public void setData(JSON data) {
        super.setData(data);
        if (data != null) {
            this.projectTemplate = data.toJavaObject(ProjectTemplateDTO.class);
        }
    }
}
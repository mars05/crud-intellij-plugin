package com.github.mars05.crud.intellij.plugin;

import com.alibaba.fastjson.JSON;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateCreateReqDTO;
import com.github.mars05.crud.intellij.plugin.service.ProjectTemplateService;
import org.junit.Test;

/**
 * @author xiaoyu
 */
public class ProjectTemplateServiceTest {
    private ProjectTemplateService projectTemplateService = new ProjectTemplateService();

    @Test
    public void getJson() {
        ProjectTemplateCreateReqDTO reqDTO = projectTemplateService.copy("1", "哈哈哈");
        System.out.println(JSON.toJSONString(reqDTO));
    }
}

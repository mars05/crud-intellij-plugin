package com.github.mars05.crud.intellij.plugin;

import com.github.mars05.crud.intellij.plugin.dto.ProjectGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectRespDTO;
import com.github.mars05.crud.intellij.plugin.service.ProjectService;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author xiaoyu
 */

public class ProjectServiceTest {
    private ProjectService projectService = new ProjectService();

    @Test
    public void generalProject() {
        ProjectGenerateReqDTO reqDTO = new ProjectGenerateReqDTO();
        reqDTO.setProjectTemplateId("1");
        reqDTO.setProjectName("test001");
        reqDTO.setBasePackage("com.mars05.test001");
        reqDTO.setGroupId("com.mars05");
        reqDTO.setArtifactId("test001");
        reqDTO.setVersion("1.0.0-SNAPSHOT");

        reqDTO.setDsId("1");
        reqDTO.setCatalog("geep");
        reqDTO.setTableNames(Arrays.asList("system_banner", "system_site"));

        ProjectRespDTO projectRespDTO = projectService.generateProject(reqDTO);
        projectService.processProjectToDisk(projectRespDTO, "E:\\Users\\98701594\\IdeaProjects");
        System.out.println(projectRespDTO);
    }
}

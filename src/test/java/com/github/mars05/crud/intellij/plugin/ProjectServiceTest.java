package com.github.mars05.crud.intellij.plugin;

import com.github.mars05.crud.intellij.plugin.dto.ProjectGenerateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectRespDTO;
import com.github.mars05.crud.intellij.plugin.service.ProjectService;
import org.junit.Test;

/**
 * @author xiaoyu
 */

public class ProjectServiceTest {
    private ProjectService projectService = new ProjectService();

    @Test
    public void generalProject() {
        ProjectGenerateReqDTO reqDTO = new ProjectGenerateReqDTO();

        ProjectRespDTO projectRespDTO = projectService.generateProject(reqDTO);
        projectService.processProjectToDisk(projectRespDTO, "E:\\Users\\98701594\\IdeaProjects");
        System.out.println(projectRespDTO);
    }
}

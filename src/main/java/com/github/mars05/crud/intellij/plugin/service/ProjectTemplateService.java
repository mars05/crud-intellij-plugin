package com.github.mars05.crud.intellij.plugin.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dao.repository.ProjectTemplateRepository;
import com.github.mars05.crud.intellij.plugin.dto.FileTemplateSaveDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateCreateReqDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateUpdateReqDTO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.util.ValidateUtils;
import com.google.common.base.Preconditions;

import java.util.List;

public class ProjectTemplateService {
    private final ProjectTemplateRepository projectTemplateRepository =
            new ProjectTemplateRepository();
    private final FileTemplateService fileTemplateService =
            new FileTemplateService();

    public void create(ProjectTemplateCreateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        if (projectTemplateRepository.list().stream().anyMatch(dataDO -> dataDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        ProjectTemplateDO dataSourceDO = BeanUtils.convertBean(reqDTO, ProjectTemplateDO.class);
        dataSourceDO.setId(IdWorker.get32UUID());
        projectTemplateRepository.create(dataSourceDO);
    }

    public ProjectTemplateCreateReqDTO copy(String projectTemplateId, String name) {
        ProjectTemplateRespDTO respDTO = detail(projectTemplateId);
        ProjectTemplateCreateReqDTO reqDTO = BeanUtils.convertBean(respDTO, ProjectTemplateCreateReqDTO.class);
        reqDTO.setName(name);
        reqDTO.setFileTemplateList(BeanUtils.convertList(respDTO.getFileTemplateList(), FileTemplateSaveDTO.class));
        return reqDTO;
    }

    public void update(ProjectTemplateUpdateReqDTO reqDTO) {
        ValidateUtils.validAnnotation(reqDTO);
        ProjectTemplateDO oldData = projectTemplateRepository.detail(reqDTO.getId());
        Preconditions.checkNotNull(oldData, "项目模板不存在");
        if (projectTemplateRepository.list().stream().anyMatch(dataDO -> !dataDO.getId().equals(reqDTO.getId()) && dataDO.getName().equals(reqDTO.getName()))) {
            throw new BizException("名称已存在");
        }
        ProjectTemplateDO dataDO = BeanUtils.convertBean(reqDTO, ProjectTemplateDO.class);
        projectTemplateRepository.update(dataDO);
    }

    public List<ProjectTemplateRespDTO> list() {
        return BeanUtils.convertList(projectTemplateRepository.list(), ProjectTemplateRespDTO.class);
    }

    public ProjectTemplateRespDTO detail(String id) {
        ProjectTemplateDO detail = projectTemplateRepository.detail(id);
        if (detail == null) {
            return null;
        }
        ProjectTemplateRespDTO respDTO = BeanUtils.convertBean(detail, ProjectTemplateRespDTO.class);
        respDTO.setFileTemplateList(fileTemplateService.list(id));
        return respDTO;
    }

    public void delete(String id) {
        ProjectTemplateDO oldDataDO = projectTemplateRepository.detail(id);
        Preconditions.checkNotNull(oldDataDO, "项目模板不存在");
        projectTemplateRepository.delete(id);
    }

}

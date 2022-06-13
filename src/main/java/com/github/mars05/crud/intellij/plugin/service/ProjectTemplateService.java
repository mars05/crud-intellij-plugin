package com.github.mars05.crud.intellij.plugin.service;

import com.alibaba.fastjson.JSON;
import com.github.mars05.crud.hub.common.dto.FileTemplateDTO;
import com.github.mars05.crud.hub.common.dto.ProjectTemplateDTO;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import com.github.mars05.crud.intellij.plugin.dao.mapper.ProjectTemplateMapper;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.dto.ProjectTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import com.google.common.base.Preconditions;

import java.util.List;

public class ProjectTemplateService {
    private final ProjectTemplateMapper projectTemplateMapper = CrudUtils.getBean(ProjectTemplateMapper.class);

    public void create(ProjectTemplateDTO reqDTO) {
        //校验
        ProjectTemplateDO newDO = BeanUtils.convertBean(reqDTO, ProjectTemplateDO.class);
        checkRepeat(newDO);
        //新增
        newDO.setFileTemplates(JSON.toJSONString(reqDTO.getFileTemplateList()));
        projectTemplateMapper.insert(newDO);
    }

    public void update(ProjectTemplateDTO reqDTO) {
        //校验
        ProjectTemplateDO oldDO = projectTemplateMapper.selectById(reqDTO.getId());
        Preconditions.checkNotNull(oldDO, "项目模板不存在");
        ProjectTemplateDO newDO = BeanUtils.convertBean(reqDTO, ProjectTemplateDO.class);
        //修改
        newDO.setFileTemplates(JSON.toJSONString(reqDTO.getFileTemplateList()));
        projectTemplateMapper.updateById(newDO);
    }

    public List<ProjectTemplateRespDTO> list() {
        return BeanUtils.convertList(projectTemplateMapper.selectList(), ProjectTemplateRespDTO.class);
    }

    public ProjectTemplateRespDTO detail(Long id) {
        ProjectTemplateDO detail = projectTemplateMapper.selectById(id);
        Preconditions.checkNotNull(detail, "项目模板不存在");
        ProjectTemplateRespDTO respDTO = BeanUtils.convertBean(detail, ProjectTemplateRespDTO.class);
        respDTO.setFileTemplateList(JSON.parseArray(detail.getFileTemplates(), FileTemplateDTO.class));
        return respDTO;
    }

    public void delete(Long id) {
        projectTemplateMapper.deleteById(id);
    }

    private void checkRepeat(ProjectTemplateDO param) {
        if (projectTemplateMapper.selectList().stream().anyMatch(projectTemplateDO ->
                projectTemplateDO.getId().equals(param.getId()))) {
            throw new BizException("模板已存在");
        }
    }

}

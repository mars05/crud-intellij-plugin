package com.github.mars05.crud.intellij.plugin.service;

import com.github.mars05.crud.intellij.plugin.dao.repository.FileTemplateRepository;
import com.github.mars05.crud.intellij.plugin.dto.FileTemplateRespDTO;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;

import java.util.List;

public class FileTemplateService {
    private final FileTemplateRepository fileTemplateRepository =
            new FileTemplateRepository();


    public List<FileTemplateRespDTO> list(String projectTemplateId) {
        return BeanUtils.convertList(fileTemplateRepository.list(projectTemplateId), FileTemplateRespDTO.class);
    }

    public FileTemplateRespDTO detail(String id) {
        return BeanUtils.convertBean(fileTemplateRepository.detail(id), FileTemplateRespDTO.class);
    }

}

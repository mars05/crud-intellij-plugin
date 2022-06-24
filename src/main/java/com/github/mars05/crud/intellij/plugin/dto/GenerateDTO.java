package com.github.mars05.crud.intellij.plugin.dto;

import com.github.mars05.crud.hub.common.dto.DataSourceDTO;
import com.github.mars05.crud.hub.common.dto.ProjectTemplateDTO;
import com.github.mars05.crud.hub.common.model.Table;
import lombok.Data;

import java.util.List;

/**
 * @author xiaoyu
 */
@Data
public class GenerateDTO {

    private ProjectTemplateDTO projectTemplate;

    private List<String> nameList;

    private String projectPath;

    private String basePackage;

    private DataSourceDTO dataSource;

    private List<Table> tables;

    private String projectName;

    private String groupId;
    private String artifactId;
    private String version;

    /**
     * 表结构来源 1:数据库表 2:DDL 3:实体类
     */
    private Integer tableSource = 0;
}
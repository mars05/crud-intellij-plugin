package com.github.mars05.crud.intellij.plugin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectGenerateReqDTO {

    private Long ptId;

    private String projectName;

    private String basePackage;

    private MavenReqDTO maven;

    private DataSourceReqDTO dataSource;

    private String ddl;

    @Data
    @Accessors(chain = true)
    public static class MavenReqDTO {
        private String groupId;
        private String artifactId;
        private String version;
    }

    @Data
    @Accessors(chain = true)
    public static class DataSourceReqDTO {
        private String databaseType;

        private String host;

        private Integer port;

        private String username;

        private String password;

        private String catalog;
        private String schema;

    }
}

package com.github.mars05.crud.intellij.plugin.setting;

import com.github.mars05.crud.intellij.plugin.dao.model.DataSourceDO;
import com.github.mars05.crud.intellij.plugin.dao.model.FileTemplateDO;
import com.github.mars05.crud.intellij.plugin.dao.model.ProjectTemplateDO;
import com.github.mars05.crud.intellij.plugin.enums.DatabaseTypeEnum;
import com.github.mars05.crud.intellij.plugin.enums.ProjectTypeEnum;
import com.github.mars05.crud.intellij.plugin.util.Constants;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyu
 */
@State(name = "CrudSettings", storages = @Storage("crud-plugin.xml"))
public class CrudSettings implements PersistentStateComponent<CrudState> {
    private CrudState myState = new CrudState();

    public static CrudSettings getInstance() {
//        return ServiceManager.getService(CrudSettings.class);
        CrudSettings crudSettings = new CrudSettings();
        crudSettings.getState().setDataSources(Arrays.asList(
                new DataSourceDO()
                        .setId("1")
                        .setName("222")
                        .setHost("10.53.173.81")
                        .setPort(3306)
                        .setUsername("root")
                        .setPassword("123456")
                        .setDatabaseType(DatabaseTypeEnum.MYSQL.getCode())
        ));
        crudSettings.getState().setProjectTemplates(Arrays.asList(
                new ProjectTemplateDO()
                        .setId("1")
                        .setName("模板1")
                        .setProjectType(ProjectTypeEnum.MAVEN.getCode())
        ));
        crudSettings.getState().setFileTemplates(Arrays.asList(
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setType(Constants.FILE_TEMPLATE_TYPE_GENERAL)
                        .setPath("pom.xml")
                        .setContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                "    <modelVersion>4.0.0</modelVersion>\n" +
                                "\n" +
                                "    <parent>\n" +
                                "        <groupId>org.springframework.boot</groupId>\n" +
                                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                                "        <version>2.1.6.RELEASE</version>\n" +
                                "    </parent>\n" +
                                "\n" +
                                "    <groupId>${maven.groupId}</groupId>\n" +
                                "    <artifactId>${maven.artifactId}</artifactId>\n" +
                                "    <version>${maven.version}</version>\n" +
                                "    <packaging>jar</packaging>\n" +
                                "\n" +
                                "    <properties>\n" +
                                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                                "        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\n" +
                                "        <java.version>1.8</java.version>\n" +
                                "    </properties>\n" +
                                "\n" +
                                "    <dependencies>\n" +
                                "        <dependency>\n" +
                                "            <groupId>org.springframework.boot</groupId>\n" +
                                "            <artifactId>spring-boot-starter-web</artifactId>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>com.baomidou</groupId>\n" +
                                "            <artifactId>mybatis-plus-boot-starter</artifactId>\n" +
                                "            <version>3.2.0</version>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>mysql</groupId>\n" +
                                "            <artifactId>mysql-connector-java</artifactId>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>org.projectlombok</groupId>\n" +
                                "            <artifactId>lombok</artifactId>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>io.springfox</groupId>\n" +
                                "            <artifactId>springfox-swagger-ui</artifactId>\n" +
                                "            <version>2.7.0</version>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>io.springfox</groupId>\n" +
                                "            <artifactId>springfox-swagger2</artifactId>\n" +
                                "            <version>2.7.0</version>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>com.alibaba</groupId>\n" +
                                "            <artifactId>druid</artifactId>\n" +
                                "            <version>1.0.5</version>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>com.alibaba</groupId>\n" +
                                "            <artifactId>fastjson</artifactId>\n" +
                                "            <version>1.2.45</version>\n" +
                                "        </dependency>\n" +
                                "        <dependency>\n" +
                                "            <groupId>org.springframework.boot</groupId>\n" +
                                "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                                "            <scope>test</scope>\n" +
                                "        </dependency>\n" +
                                "    </dependencies>\n" +
                                "\n" +
                                "    <build>\n" +
                                "        <plugins>\n" +
                                "            <plugin>\n" +
                                "                <groupId>org.springframework.boot</groupId>\n" +
                                "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                                "            </plugin>\n" +
                                "        </plugins>\n" +
                                "    </build>\n" +
                                "\n" +
                                "</project>\n"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/Application.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_GENERAL)
                        .setContent("package ${project.basePackage};\n" +
                                "\n" +
                                "import org.mybatis.spring.annotation.MapperScan;\n" +
                                "import org.springframework.boot.SpringApplication;\n" +
                                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                                "import org.springframework.boot.context.properties.EnableConfigurationProperties;\n" +
                                "import org.springframework.cache.annotation.EnableCaching;\n" +
                                "import org.springframework.transaction.annotation.EnableTransactionManagement;\n" +
                                "\n" +
                                "@SpringBootApplication\n" +
                                "@MapperScan(\"${project.basePackage}.dao\")\n" +
                                "public class Application {\n" +
                                "\n" +
                                "    public static void main(String[] args) {\n" +
                                "        SpringApplication.run(Application.class, args);\n" +
                                "    }\n" +
                                "}\n"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/resources/application.yml")
                        .setType(Constants.FILE_TEMPLATE_TYPE_GENERAL)
                        .setContent("spring:\n" +
                                "  datasource:\n" +
                                "    url: jdbc:mysql://${dataSource.host}:${dataSource.port?c}/${dataSource.catalog}?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n" +
                                "    username: ${dataSource.username}\n" +
                                "    password: ${dataSource.password}\n" +
                                "    hikari:\n" +
                                "      minimum-idle: 5\n" +
                                "      maximum-pool-size: 100\n" +
                                "      idle-timeout: 30000\n" +
                                "      validation-timeout: 250\n" +
                                "      max-lifetime: 1800000\n" +
                                "      connection-timeout: 30000\n" +
                                "      connection-test-query: SELECT 1\n" +
                                "  jackson:\n" +
                                "    date-format: yyyy-MM-dd HH:mm:ss\n" +
                                "    time-zone: GMT+8\n"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/config/Swagger2Config.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_GENERAL)
                        .setContent("package ${project.basePackage+'.config'};\n" +
                                "\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import springfox.documentation.builders.ApiInfoBuilder;\n" +
                                "import springfox.documentation.builders.RequestHandlerSelectors;\n" +
                                "import springfox.documentation.service.ApiInfo;\n" +
                                "import springfox.documentation.spi.DocumentationType;\n" +
                                "import springfox.documentation.spring.web.plugins.Docket;\n" +
                                "import springfox.documentation.swagger2.annotations.EnableSwagger2;\n" +
                                "\n" +
                                "/**\n" +
                                " * Swagger配置\n" +
                                " */\n" +
                                "@Configuration\n" +
                                "@EnableSwagger2\n" +
                                "public class Swagger2Config {\n" +
                                "    @Bean\n" +
                                "    public Docket docket() {\n" +
                                "        return new Docket(DocumentationType.SWAGGER_2)\n" +
                                "                .apiInfo(apiInfo())\n" +
                                "                .select()\n" +
                                "                .apis(RequestHandlerSelectors.basePackage(\"${project.basePackage+'.controller'}\"))\n" +
                                "                .build();\n" +
                                "    }\n" +
                                "\n" +
                                "    private ApiInfo apiInfo() {\n" +
                                "        return new ApiInfoBuilder()\n" +
                                "                .title(\"${maven.artifactId} API文档\")\n" +
                                "                .description(\"${maven.artifactId} API文档\")\n" +
                                "                .version(\"${maven.version}\")\n" +
                                "                .build();\n" +
                                "    }\n" +
                                "}\n"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/model/${table.upperCamelName}.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_CODE)
                        .setContent("package ${project.basePackage}.model;\n" +
                                "\n" +
                                "import lombok.Data;\n" +
                                "import com.baomidou.mybatisplus.annotation.TableName;\n" +
                                "import com.baomidou.mybatisplus.annotation.TableId;\n" +
                                "import com.baomidou.mybatisplus.annotation.TableField;\n" +
                                "<#assign columns=table.columns />\n" +
                                "<#list table.imports as import>\n" +
                                "import ${import};\n" +
                                "</#list>" +
                                "\n" +
                                "/**\n" +
                                "* ${table.remarks}\n" +
                                "*/\n" +
                                "@Data\n" +
                                "@TableName(\"${table.tableName}\")\n" +
                                "public class ${table.upperCamelName} {\n" +
                                "<#list columns as column>\n" +
                                "    /**\n" +
                                "    * ${column.remarks}\n" +
                                "    */\n" +
                                "    <#if column.primaryKey>\n" +
                                "    @TableId\n" +
                                "    </#if>\n" +
                                "    @TableField(\"${column.columnName}\")\n" +
                                "    private ${column.javaTypeClass.simpleName} ${column.lowerCamelName};\n" +
                                "</#list>\n" +
                                "}"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/dao/${table.upperCamelName}Mapper.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_CODE)
                        .setContent("package ${project.basePackage}.dao;\n" +
                                "\n" +
                                "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n" +
                                "import ${project.basePackage}.model.${table.upperCamelName};\n" +
                                "\n" +
                                "/**\n" +
                                " * ${table.remarks}\n" +
                                " */\n" +
                                "public interface ${table.upperCamelName}Mapper extends BaseMapper<${table.upperCamelName}> {\n" +
                                "\n" +
                                "}"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/service/${table.upperCamelName}Service.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_CODE)
                        .setContent("package ${project.basePackage}.service;\n" +
                                "\n" +
                                "import com.baomidou.mybatisplus.extension.service.IService;\n" +
                                "import com.baomidou.mybatisplus.core.metadata.IPage;\n" +
                                "import ${project.basePackage}.model.${table.upperCamelName};\n" +
                                "\n" +
                                "/**\n" +
                                " * ${table.remarks}\n" +
                                " */\n" +
                                "public interface ${table.upperCamelName}Service extends IService<${table.upperCamelName}> {\n" +
                                "\n" +
                                "<#list table.columns as column>\n" +
                                "    <#if column.primaryKey>\n" +
                                "    /**\n" +
                                "     * 通过ID查询单个${table.remarks}\n" +
                                "     *\n" +
                                "     * @param id ID\n" +
                                "     * @return {@link ${table.upperCamelName}}\n" +
                                "     */\n" +
                                "     ${table.upperCamelName} findById(${column.javaTypeClass.simpleName} id);\n" +
                                "\n" +
                                "    /**\n" +
                                "     * 分页查询${table.remarks}\n" +
                                "     *\n" +
                                "     * @param pageNum   页号\n" +
                                "     * @param pageSize 每页大小\n" +
                                "     * @return {@link ${table.upperCamelName}}\n" +
                                "     */\n" +
                                "     IPage<${table.upperCamelName}> findByPage(int pageNum, int pageSize);\n" +
                                "\n" +
                                "    /**\n" +
                                "     * 新增${table.remarks}\n" +
                                "     *\n" +
                                "     * @param ${table.lowerCamelName} ${table.upperCamelName}\n" +
                                "     */\n" +
                                "    void insert(${table.upperCamelName} ${table.lowerCamelName});\n" +
                                "\n" +
                                "    /**\n" +
                                "     * 修改${table.remarks}\n" +
                                "     *\n" +
                                "     * @param ${table.lowerCamelName} ${table.upperCamelName}\n" +
                                "     */\n" +
                                "    void update(${table.upperCamelName} ${table.lowerCamelName});\n" +
                                "\n" +
                                "    /**\n" +
                                "     * 通过ID删除单个${table.remarks}\n" +
                                "     *\n" +
                                "     * @param id ID\n" +
                                "     */\n" +
                                "    void deleteById(${column.javaTypeClass.simpleName} id);\n" +
                                "    </#if>\n" +
                                "</#list>\n" +
                                "\n" +
                                "}"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/service/impl/${table.upperCamelName}ServiceImpl.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_CODE)
                        .setContent("package ${project.basePackage}.service.impl;\n" +
                                "\n" +
                                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                                "import com.baomidou.mybatisplus.core.metadata.IPage;\n" +
                                "import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n" +
                                "import org.springframework.stereotype.Service;\n" +
                                "import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n" +
                                "import ${project.basePackage}.model.${table.upperCamelName};\n" +
                                "import ${project.basePackage}.dao.${table.upperCamelName}Mapper;\n" +
                                "import ${project.basePackage}.service.${table.upperCamelName}Service;\n" +
                                "\n" +
                                "@Service\n" +
                                "public class ${table.upperCamelName}ServiceImpl extends ServiceImpl<${table.upperCamelName}Mapper, ${table.upperCamelName}> implements ${table.upperCamelName}Service{\n" +
                                "\n" +
                                "<#list table.columns as column>\n" +
                                "    <#if column.primaryKey>\n" +
                                "    @Autowired\n" +
                                "    private ${table.upperCamelName}Mapper ${table.lowerCamelName}Mapper;\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public ${table.upperCamelName} findById(${column.javaTypeClass.simpleName} id){\n" +
                                "        return ${table.lowerCamelName}Mapper.selectById(id);\n" +
                                "    }\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public IPage<${table.upperCamelName}> findByPage(int pageNum, int pageSize) {\n" +
                                "        return ${table.lowerCamelName}Mapper.selectPage(new Page<>(pageNum, pageSize), null);\n" +
                                "    }\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public void insert(${table.upperCamelName} ${table.lowerCamelName}){\n" +
                                "        ${table.lowerCamelName}Mapper.insert(${table.lowerCamelName});\n" +
                                "    }\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public void update(${table.upperCamelName} ${table.lowerCamelName}){\n" +
                                "        ${table.lowerCamelName}Mapper.updateById(${table.lowerCamelName});\n" +
                                "    }\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public void deleteById(${column.javaTypeClass.simpleName} id){\n" +
                                "        ${table.lowerCamelName}Mapper.deleteById(id);\n" +
                                "    }\n" +
                                "    </#if>\n" +
                                "</#list>\n" +
                                "\n" +
                                "}"),
                new FileTemplateDO()
                        .setProjectTemplateId("1")
                        .setPath("src/main/java/${project.basePackageDir}/controller/${table.upperCamelName}Controller.java")
                        .setType(Constants.FILE_TEMPLATE_TYPE_CODE)
                        .setContent("package ${project.basePackage}.controller;\n" +
                                "\n" +
                                "import io.swagger.annotations.Api;\n" +
                                "import io.swagger.annotations.ApiOperation;\n" +
                                "import io.swagger.annotations.ApiParam;\n" +
                                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                                "import com.baomidou.mybatisplus.core.metadata.IPage;\n" +
                                "import org.springframework.web.bind.annotation.DeleteMapping;\n" +
                                "import org.springframework.web.bind.annotation.GetMapping;\n" +
                                "import org.springframework.web.bind.annotation.PathVariable;\n" +
                                "import org.springframework.web.bind.annotation.PostMapping;\n" +
                                "import org.springframework.web.bind.annotation.PutMapping;\n" +
                                "import org.springframework.web.bind.annotation.RequestBody;\n" +
                                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                                "import org.springframework.web.bind.annotation.RequestParam;\n" +
                                "import org.springframework.web.bind.annotation.RestController;\n" +
                                "import ${project.basePackage}.model.${table.upperCamelName};\n" +
                                "import ${project.basePackage}.service.${table.upperCamelName}Service;\n" +
                                "\n" +
                                "<#list table.columns as column>\n" +
                                "<#if column.primaryKey>\n" +
                                "/**\n" +
                                " * ${table.remarks}\n" +
                                " */\n" +
                                "@RestController\n" +
                                "@RequestMapping(\"/${table.lowerCamelName}\")\n" +
                                "@Api(tags = \"${table.remarks}\")\n" +
                                "public class ${table.upperCamelName}Controller {\n" +
                                "    @Autowired\n" +
                                "    private ${table.upperCamelName}Service ${table.lowerCamelName}Service;\n" +
                                "\n" +
                                "    @GetMapping(\"/{id}\")\n" +
                                "    @ApiOperation(\"通过ID查询单个${table.remarks}\")\n" +
                                "    public ${table.upperCamelName} findById(@ApiParam(\"ID\") @PathVariable(\"id\") ${column.javaTypeClass.simpleName} id) {\n" +
                                "        return ${table.lowerCamelName}Service.findById(id);\n" +
                                "    }\n" +
                                "\n" +
                                "    @GetMapping\n" +
                                "    @ApiOperation(\"分页查询${table.remarks}\")\n" +
                                "    public IPage<${table.upperCamelName}> findByPage(@ApiParam(\"页号\") @RequestParam(defaultValue = \"1\") Integer pageNum,\n" +
                                "                                                @ApiParam(\"每页大小\") @RequestParam(defaultValue = \"10\") Integer pageSize) {\n" +
                                "        return ${table.lowerCamelName}Service.findByPage(pageNum, pageSize);\n" +
                                "    }\n" +
                                "\n" +
                                "    @PostMapping\n" +
                                "    @ApiOperation(\"新增${table.remarks}\")\n" +
                                "    public void insert(@RequestBody ${table.upperCamelName} ${table.lowerCamelName}) {\n" +
                                "        ${table.lowerCamelName}Service.insert(${table.lowerCamelName});\n" +
                                "    }\n" +
                                "\n" +
                                "    @PutMapping\n" +
                                "    @ApiOperation(\"修改${table.remarks}\")\n" +
                                "    public void update(@RequestBody ${table.upperCamelName} ${table.lowerCamelName}) {\n" +
                                "        ${table.lowerCamelName}Service.update(${table.lowerCamelName});\n" +
                                "    }\n" +
                                "\n" +
                                "    @DeleteMapping(\"/{id}\")\n" +
                                "    @ApiOperation(\"通过ID删除单个${table.remarks}\")\n" +
                                "    public void deleteById(@ApiParam(\"ID\") @PathVariable(\"id\") ${column.javaTypeClass.simpleName} id) {\n" +
                                "        ${table.lowerCamelName}Service.deleteById(id);\n" +
                                "    }\n" +
                                "}\n" +
                                "    </#if>\n" +
                                "</#list>")
        ));
        return crudSettings;
    }

    @Nullable
    @Override
    public CrudState getState() {
        return myState;
    }

    @Override
    public void loadState(CrudState state) {
        myState = state;
    }

    public List<Conn> getConns() {
        return myState.getConns();
    }

    public Map<String, SelectionSaveInfo> getSelectionSaveInfoMap() {
        return myState.getSelectionSaveInfoMap();
    }
}

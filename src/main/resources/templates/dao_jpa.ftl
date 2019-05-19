package ${package};

import org.springframework.data.jpa.repository.JpaRepository;
<#list imports as import>
    import ${import};
</#list>

/**
 * ${comment}
 */
<#list model.fields as field>
    <#if field.id>
public interface ${simpleName} extends JpaRepository<${model.simpleName}, ${field.typeSimpleName}> {

}
    </#if>
</#list>
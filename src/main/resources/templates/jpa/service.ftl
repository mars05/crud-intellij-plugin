package ${package};

import org.springframework.data.domain.Page;
<#list imports as import>
    import ${import};
</#list>

/**
 * ${comment}
 */
public interface ${simpleName} {

<#assign model=dao.model />
<#list model.fields as field>
    <#if field.id>
    /**
     * 通过ID查询单个${model.comment}
     *
     * @param id ID
     * @return {@link ${model.simpleName}}
     */
     ${model.simpleName} findById(${field.typeSimpleName} id);

    /**
     * 分页查询${model.comment}
     *
     * @param pageNum   页号
     * @param pageSize 每页大小
     * @return {@link ${model.simpleName}}
     */
     Page<${model.simpleName}> findByPage(int pageNum, int pageSize);

    /**
     * 新增${model.comment}
     *
     * @param ${model.varName} ${model.comment}
     */
    void insert(${model.simpleName} ${model.varName});

    /**
     * 修改${model.comment}
     *
     * @param ${model.varName} ${model.comment}
     */
    void update(${model.simpleName} ${model.varName});

    /**
     * 通过ID删除单个${model.comment}
     *
     * @param id ID
     */
    void deleteById(${field.typeSimpleName} id);
    </#if>
</#list>

}
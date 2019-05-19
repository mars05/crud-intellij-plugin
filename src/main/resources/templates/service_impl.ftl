package ${package+'.impl'};

import org.springframework.beans.factory.annotation.Autowired;
<#if ormType==0>
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
<#else>
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
</#if>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<#list imports as import>
    import ${import};
</#list>

@Service
@Transactional(rollbackFor = Exception.class)
public class ${simpleName}Impl implements ${simpleName}{

<#assign model=dao.model />
<#list model.fields as field>
    <#if field.id>
    @Autowired
    private ${dao.simpleName} ${dao.varName};

    @Transactional(readOnly = true)
    @Override
    public ${model.simpleName} findById(${field.typeSimpleName} id){
        <#if ormType==0>
        return ${dao.varName}.findById(id);
        <#else >
        return ${dao.varName}.findById(id).orElse(null);
        </#if>
    }

    @Transactional(readOnly = true)
    @Override
    <#if ormType==0>
    public PageInfo<${model.simpleName}> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(${dao.varName}.findByPage());
    }
    <#else>
    public Page<${model.simpleName}> findByPage(int pageNum, int pageSize) {
        return ${dao.varName}.findAll(PageRequest.of(pageNum, pageSize));
    }
    </#if>

    @Override
    public void insert(${model.simpleName} ${model.varName}){
        <#if ormType==0>
        ${dao.varName}.insert(${model.varName});
        <#else >
        ${dao.varName}.save(${model.varName});
        </#if>
    }

    @Override
    public void update(${model.simpleName} ${model.varName}){
        <#if ormType==0>
        ${dao.varName}.update(${model.varName});
        <#else >
        ${dao.varName}.save(${model.varName});
        </#if>
    }

    @Override
    public void deleteById(${field.typeSimpleName} id){
        ${dao.varName}.deleteById(id);
    }
    </#if>
</#list>

}
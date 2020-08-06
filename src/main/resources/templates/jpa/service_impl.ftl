package ${package+'.impl'};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return ${dao.varName}.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<${model.simpleName}> findByPage(int pageNum, int pageSize) {
        return ${dao.varName}.findAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public void insert(${model.simpleName} ${model.varName}){
        ${dao.varName}.save(${model.varName});
    }

    @Override
    public void update(${model.simpleName} ${model.varName}){
        ${dao.varName}.save(${model.varName});
    }

    @Override
    public void deleteById(${field.typeSimpleName} id){
        ${dao.varName}.deleteById(id);
    }
    </#if>
</#list>

}
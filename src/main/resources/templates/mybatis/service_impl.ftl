package ${package+'.impl'};

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        return ${dao.varName}.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<${model.simpleName}> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageInfo.of(${dao.varName}.findByPage());
    }

    @Override
    public void insert(${model.simpleName} ${model.varName}){
        ${dao.varName}.insert(${model.varName});
    }

    @Override
    public void update(${model.simpleName} ${model.varName}){
        ${dao.varName}.update(${model.varName});
    }

    @Override
    public void deleteById(${field.typeSimpleName} id){
        ${dao.varName}.deleteById(id);
    }
    </#if>
</#list>

}
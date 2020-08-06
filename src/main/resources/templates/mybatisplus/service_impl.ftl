package ${package+'.impl'};

import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
<#list imports as import>
    import ${import};
</#list>

<#assign model=dao.model />
@Service
@Transactional(rollbackFor = Exception.class)
public class ${simpleName}Impl extends ServiceImpl<${dao.simpleName}, ${model.simpleName}> implements ${simpleName}{

<#list model.fields as field>
    <#if field.id>
    @Autowired
    private ${dao.simpleName} ${dao.varName};

    @Transactional(readOnly = true)
    @Override
    public ${model.simpleName} findById(${field.typeSimpleName} id){
        return ${dao.varName}.selectById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public IPage<${model.simpleName}> findByPage(int pageNum, int pageSize) {
        return ${dao.varName}.selectPage(new Page<>(pageNum, pageSize), null);
    }

    @Override
    public void insert(${model.simpleName} ${model.varName}){
        ${dao.varName}.insert(${model.varName});
    }

    @Override
    public void update(${model.simpleName} ${model.varName}){
        ${dao.varName}.updateById(${model.varName});
    }

    @Override
    public void deleteById(${field.typeSimpleName} id){
        ${dao.varName}.deleteById(id);
    }
    </#if>
</#list>

}
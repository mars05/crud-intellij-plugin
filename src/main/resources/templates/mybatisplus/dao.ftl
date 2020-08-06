package ${package};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
<#list imports as import>
    import ${import};
</#list>

/**
 * ${comment}
 */
public interface ${simpleName} extends BaseMapper<${model.simpleName}> {

}
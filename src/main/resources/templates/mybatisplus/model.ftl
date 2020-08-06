package ${package};

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
<#list imports as import>
    import ${import};
</#list>

/**
* ${comment}
*/
@Data
@TableName("${tableName}")
public class ${simpleName} {
<#list fields as field>
    /**
    * ${field.comment}
    */
    <#if field.id>
        @TableId
    </#if>
    @TableField("${field.columnName}")
    private ${field.typeSimpleName} ${field.name};
</#list>
}
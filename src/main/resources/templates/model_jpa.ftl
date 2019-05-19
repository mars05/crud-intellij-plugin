package ${package};

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
<#list imports as import>
    import ${import};
</#list>

/**
 * ${comment}
 */
@Data
@Entity
@Table(name = "${tableName}")
public class ${simpleName} {
<#list fields as field>
    /**
     * ${field.comment}
     */
    <#if field.id>
    @Id
    </#if>
    @Column(name = "${field.columnName}")
    private ${field.typeSimpleName} ${field.name};
</#list>
}
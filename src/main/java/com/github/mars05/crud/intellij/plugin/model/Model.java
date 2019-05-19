package com.github.mars05.crud.intellij.plugin.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaoyu
 */
public class Model extends Base {
    private String tableName;
    private List<Field> fields;

    /**
     * @param comment   类的注释
     * @param name      类的全限定名
     * @param tableName 表的名称
     */
    public Model(String comment, String name, String tableName, List<Field> fields) {
        super(comment, name);
        this.tableName = tableName;
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        List<Field> fields = getFields();
        for (Field field : fields) {
            if (field.isImport()) {
                imports.add(field.getTypeName());
            }
        }
        return imports;
    }
}

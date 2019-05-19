package com.github.mars05.crud.intellij.plugin.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaoyu
 */
public class Dao extends Base {

    private Model model;

    /**
     * @param comment 类的注释
     * @param name    类的全限定名
     */
    public Dao(String comment, String name, Model model) {
        super(comment, name);
        this.model = model;
        setOrmType(model.getOrmType());
    }

    public Model getModel() {
        return model;
    }

    @Override
    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        imports.add(model.getName());
        List<Field> fields = model.getFields();
        for (Field field : fields) {
            if (field.isId() && field.isImport()) {
                imports.add(field.getTypeName());
                break;
            }
        }
        return imports;
    }
}

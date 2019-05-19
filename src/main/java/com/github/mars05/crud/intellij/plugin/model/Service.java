package com.github.mars05.crud.intellij.plugin.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaoyu
 */
public class Service extends Base {
    private Dao dao;

    /**
     * @param comment 类的注释
     * @param name    类的全限定名
     */
    public Service(String comment, String name, Dao dao) {
        super(comment, name);
        this.dao = dao;
        setOrmType(dao.getOrmType());
    }

    public Dao getDao() {
        return dao;
    }

    private Set<String> imports = new HashSet<>();

    @Override
    public Set<String> getImports() {
        Model model = dao.getModel();
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

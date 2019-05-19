package com.github.mars05.crud.intellij.plugin.model;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @author xiaoyu
 */
public abstract class Base {
    private int ormType;
    private String comment;
    private String name;

    /**
     * @param comment 类的注释
     * @param name    类的全限定名
     */
    public Base(String comment, String name) {
        this.comment = comment;
        this.name = name;
    }

    public String getPackage() {
        return StringUtils.substringBeforeLast(name, ".");
    }

    public abstract Set<String> getImports();

    public String getComment() {
        return comment;
    }

    public String getSimpleName() {
        return name.lastIndexOf(".") == -1 ? name : StringUtils.substringAfterLast(name, ".");
    }

    public String getVarName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getSimpleName());
    }

    public String getName() {
        return name;
    }

    public void setOrmType(int ormType) {
        this.ormType = ormType;
    }

    public int getOrmType() {
        return ormType;
    }
}

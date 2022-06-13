package com.github.mars05.crud.intellij.plugin.dao.mapper;

import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaoyu
 */
public abstract class AbstractMapper<T> {
    @NotNull
    protected abstract List<T> getDataList();

    @NotNull
    protected abstract Class<T> getDataClass();

    protected abstract Serializable getId(T t);

    public T selectById(Serializable id) {
        T data = getDataList().stream().filter(dataDO -> getId(dataDO).equals(id)).findFirst().orElse(null);
        return BeanUtils.convertBean(data, getDataClass());
    }


    public List<T> selectList() {
        return BeanUtils.convertList(getDataList(), getDataClass());
    }

    public void insert(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (null == getId(data)) {
            throw new BizException("ID不能为空");
        }
        if (getDataList().stream().anyMatch(oldDO -> getId(oldDO).equals(getId(newDO)))) {
            throw new BizException("ID冲突");
        }
        getDataList().add(newDO);
    }

    public void updateById(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (null == getId(newDO)) {
            throw new BizException("ID不能为空");
        }
        T oldDO = getDataList().stream().filter(old -> getId(old).equals(getId(newDO))).findFirst().orElse(null);
        if (oldDO == null) {
            throw new BizException("目标不存在,ID:" + getId(newDO));
        }
        BeanUtils.copyProperties(newDO, oldDO);
    }

    public void deleteById(Serializable id) {
        int delIndex = -1;
        for (int i = 0; i < getDataList().size(); i++) {
            if (getId(getDataList().get(i)).equals(id)) {
                delIndex = i;
                break;
            }
        }
        if (delIndex < 0) {
            throw new BizException("目标不存在,ID:" + id);
        }
        getDataList().remove(delIndex);
    }

}

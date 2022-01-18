package com.github.mars05.crud.intellij.plugin.dao.repository;

import com.github.mars05.crud.intellij.plugin.dao.model.BaseDO;
import com.github.mars05.crud.intellij.plugin.exception.BizException;
import com.github.mars05.crud.intellij.plugin.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author xiaoyu
 */
public abstract class AbstractRepository<T extends BaseDO> {
    @NotNull
    protected abstract List<T> getDataList();

    @NotNull
    protected abstract Class<T> getDataClass();

    public T detail(String id) {
        T data = getDataList().stream().filter(dataDO -> dataDO.getId().equals(id)).findFirst().orElse(null);
        return BeanUtils.convertBean(data, getDataClass());
    }

    public List<T> list() {
        return BeanUtils.convertList(getDataList(), getDataClass());
    }

    public void create(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (StringUtils.isBlank(newDO.getId())) {
            throw new BizException("ID不能为空");
        }
        if (getDataList().stream().allMatch(oldDO -> oldDO.getId().equals(newDO.getId()))) {
            throw new BizException("ID冲突");
        }
        getDataList().add(newDO);
    }

    public void update(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (StringUtils.isBlank(newDO.getId())) {
            throw new BizException("ID不能为空");
        }
        T oldDO = getDataList().stream().filter(old -> old.getId().equals(newDO.getId())).findFirst().orElse(null);
        if (oldDO == null) {
            throw new BizException("目标不存在,ID:" + newDO.getId());
        }
        BeanUtils.copyProperties(newDO, oldDO);
    }

    public void delete(String id) {
        int delIndex = -1;
        for (int i = 0; i < getDataList().size(); i++) {
            if (getDataList().get(i).getId().equals(id)) {
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

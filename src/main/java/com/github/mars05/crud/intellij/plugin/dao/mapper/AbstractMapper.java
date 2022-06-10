package com.github.mars05.crud.intellij.plugin.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.hub.common.util.BeanUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoyu
 */
public abstract class AbstractMapper<T> implements BaseMapper<T> {
    @NotNull
    protected abstract List<T> getDataList();

    @NotNull
    protected abstract Class<T> getDataClass();

    protected abstract Serializable getId(T t);

    @Override
    public T selectById(Serializable id) {
        T data = getDataList().stream().filter(dataDO -> getId(dataDO).equals(id)).findFirst().orElse(null);
        return BeanUtils.convertBean(data, getDataClass());
    }

    @Override
    public List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Long selectCount(Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public List<T> selectList(Wrapper<T> queryWrapper) {
        return BeanUtils.convertList(getDataList(), getDataClass());
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public List<Object> selectObjs(Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<T>> P selectPage(P page, Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public int insert(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (null == getId(data)) {
            throw new BizException("ID不能为空");
        }
        if (getDataList().stream().anyMatch(oldDO -> getId(oldDO).equals(getId(newDO)))) {
            throw new BizException("ID冲突");
        }
        getDataList().add(newDO);
        return 1;
    }

    @Override
    public int updateById(T data) {
        T newDO = BeanUtils.convertBean(data, getDataClass());
        if (null == getId(newDO)) {
            throw new BizException("ID不能为空");
        }
        T oldDO = getDataList().stream().filter(old -> getId(old).equals(getId(newDO))).findFirst().orElse(null);
        if (oldDO == null) {
            throw new BizException("目标不存在,ID:" + getId(newDO));
        }
        BeanUtils.copyProperties(newDO, oldDO);
        return 1;
    }

    @Override
    public int update(T entity, Wrapper<T> updateWrapper) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
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
        return 1;
    }

    @Override
    public int deleteById(T entity) {
        return 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return 0;
    }

    @Override
    public int delete(Wrapper<T> queryWrapper) {
        return 0;
    }

    @Override
    public int deleteBatchIds(Collection<?> idList) {
        return 0;
    }
}

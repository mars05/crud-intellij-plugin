package com.github.mars05.crud.intellij.plugin.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanUtils
 *
 * @author xiaoyu
 */
public class BeanUtils {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <E, T> void copyProperties(E source, T destination) {
        modelMapper.map(source, destination);
    }

    public static <E, T> T convertBean(E source, Class<T> destinationType) {
        if (null == source) {
            return null;
        }
        return modelMapper.map(source, destinationType);
    }

    public static <E, T> List<T> convertList(List<E> sourceList, Class<T> destinationType) {
        List<T> list = new ArrayList<>();
        if (sourceList == null || sourceList.isEmpty()) {
            return list;
        }
        sourceList.forEach(o -> {
            list.add(convertBean(o, destinationType));
        });
        return list;
    }

}

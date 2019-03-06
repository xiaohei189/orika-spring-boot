package com.weirui.orika;

import java.util.Map;

/**
 * MapperFactory 配置属性
 *
 * @author 隗锐
 * @dateTime 2019-01-09 18:31:46
 */
public class MapperFactoryConfigAttribute {


    private Class<?> source;
    private Map<String ,MapperField> mapperFields;

    private Mapper mapper;

    public Map<String, MapperField> getMapperFields() {
        return mapperFields;
    }

    public void setMapperFields(Map<String, MapperField> mapperFields) {
        this.mapperFields = mapperFields;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public Class<?> getSource() {
        return source;
    }

    public void setSource(Class<?> source) {
        this.source = source;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}

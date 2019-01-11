package com.weirui.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.SmartFactoryBean;

/**
 * @author 隗锐
 * @dateTime 2019-01-06 15:55:12
 */
public class MapperFactoryFactoryBean implements SmartFactoryBean<MapperFactory> {

    @Override
    public MapperFactory getObject() throws Exception {
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();
        DefaultMapperFactory mapperFactory = builder.build();

        return mapperFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultMapperFactory.class;
    }

}

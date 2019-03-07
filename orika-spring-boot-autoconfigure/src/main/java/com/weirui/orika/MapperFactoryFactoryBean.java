package com.weirui.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * MapperFactory 的FactoryBean，衔接orika与ioc，将实例交给容器管理，并对MapperFactory做一些配置，通过扫描到映射信息
 *
 * @author 隗锐
 * @dateTime 2019-01-06 15:55:12
 */
public class MapperFactoryFactoryBean implements SmartFactoryBean<MapperFactory> ,ApplicationContextAware {

    private final Logger logger=LoggerFactory.getLogger(MapperFactoryFactoryBean.class);


    private final String[] packages;

    private ApplicationContext applicationContext;

    public MapperFactoryFactoryBean(String[] packages) {
        this.packages=packages;
    }

    @Override
    public MapperFactory getObject() throws Exception {
        DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();
        DefaultMapperFactory mapperFactory = builder.build();

        ConverterFactory converterFactory = mapperFactory.getConverterFactory();

        ClassPathMapperBeanScanner classPathMapperBeanScanner = new ClassPathMapperBeanScanner();

        Set<MapperFactoryConfigAttribute> beanConfigAttributes = classPathMapperBeanScanner.scan(this.packages);

        beanConfigAttributes.forEach(attribute->{
            Map<String, MapperField> mapperFields = attribute.getMapperFields();
            Mapper mapper = attribute.getMapper();
            Class<?> source = attribute.getSource();
            Class<?> destination = mapper.destination();


            ClassMapBuilder<?, ?> classMapBuilder = mapperFactory
                    .classMap(source, destination)
                    .mapNulls(mapper.mapNull())
                    .mapNullsInReverse(mapper.mapNullsInReverse());


            String[] excludes = mapper.excludes();
            for (String exclude : excludes) {
                classMapBuilder.exclude(exclude);
            }

            Set<String> sourcesFields = mapperFields.keySet();

            for (String field : sourcesFields) {

                MapperField mapperField = mapperFields.get(field);
                FieldMapBuilder<?, ?> fieldMapBuilder = classMapBuilder.fieldMap(field, mapperField.fieldName())
                        .mapNulls(mapperField.mapNull())
                        .mapNullsInReverse(mapperField.mapNullsInReverse());

                String converterName = mapperField.converterName();

                if (!StringUtils.isEmpty(converterName)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("register field convert {} of {} among {} of {},convert id is {} ",
                                field, source.getCanonicalName(), mapperField.fieldName(), destination.getCanonicalName(), converterName);
                    }

                    BidirectionalConverter converter = applicationContext.getBean(converterName, BidirectionalConverter.class);

                    Assert.notNull(converter,"BidirectionalConverter is not found that name is "+converterName+" in applicationContext");

                    converterFactory.registerConverter(converterName,converter);

                    fieldMapBuilder.converter(converterName);

                }



            }

            classMapBuilder.byDefault().register();

        });

        return mapperFactory;
    }


    @Override
    public Class<?> getObjectType() {
        return DefaultMapperFactory.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext=applicationContext;
    }

}

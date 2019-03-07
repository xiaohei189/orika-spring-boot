package com.weirui.orika;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(OrikaProperties.class)
public class OrikaAutoConfiguration {

    @Autowired
    OrikaProperties orikaProperties;
    @Bean
    @ConditionalOnMissingBean(MapperFactoryFactoryBean.class)
    public MapperFactoryFactoryBean mapperFactoryFactoryBean() {
        String packageName = orikaProperties.getPackageName();
        Objects.requireNonNull(packageName,"scan package must not null");

        String[] packages = packageName.split(",");
        MapperFactoryFactoryBean factoryBean = new MapperFactoryFactoryBean(packages);
        return factoryBean;
    }
}

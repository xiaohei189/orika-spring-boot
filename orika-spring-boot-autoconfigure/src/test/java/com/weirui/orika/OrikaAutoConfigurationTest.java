package com.weirui.orika;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.plaf.basic.BasicPanelUI;

@SpringBootTest(classes = {OrikaAutoConfigurationTest.Config.class})
@RunWith(SpringRunner.class)
public class OrikaAutoConfigurationTest {
    @Autowired
    private MapperFactory mapperFactory;

    @Test
    public void defaultCopyTest() {
        Source source = new Source("小黑", 123);

        Source.Dest dest = mapperFactory.getMapperFacade().map(source, Source.Dest.class);

        Assert.assertEquals(source.getName(),dest.getName());
        Assert.assertEquals(source.getAge(),dest.getAge());
        Assert.assertTrue(dest.getAlia()==null);
    }

    @Test
    public void aliasNameCopyTest() {
        SourceAlias source = new SourceAlias("小黑", 123);
        SourceAlias.Dest dest = mapperFactory.getMapperFacade().map(source, SourceAlias.Dest.class);

        Assert.assertEquals(source.getName(),dest.getAlia());
        Assert.assertEquals(source.getAge(),dest.getAge());
        Assert.assertTrue(dest.getName()==null);
    }
    @Test
    public void typeConvertCopyTest() {
        SourceConvert source = new SourceConvert("小黑", "123");

        SourceConvert.Dest dest = mapperFactory.getMapperFacade().map(source, SourceConvert.Dest.class);

        Assert.assertEquals(source.getName(),dest.getName());
        Assert.assertEquals(123,dest.getAge());
    }


    @Configuration
    @ImportAutoConfiguration(OrikaAutoConfiguration.class)
    static class Config {
        @Bean(name = "string2Number")
        public BidirectionalConverter bidirectionalConverter() {
            return new MyConverter();
        }
    }

}

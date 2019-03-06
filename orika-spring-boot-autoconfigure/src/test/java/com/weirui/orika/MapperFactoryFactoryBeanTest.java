package com.weirui.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.junit.Assert;
import org.junit.Test;

public class MapperFactoryFactoryBeanTest {
    @Test
    public void fieldMapper() throws Exception {

        MapperFactoryFactoryBean mapperFactoryFactoryBean = new MapperFactoryFactoryBean();

        MapperFactory mapperFactory = mapperFactoryFactoryBean.getObject();

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        Source source = new Source("123",2);

        Dest des = mapperFacade.map(source, Dest.class);
        Assert.assertTrue(des.getName().equals("123"));
        Assert.assertTrue(des.getAge()==2);


    }

    @Test
    public void fieldExcludeMapper() throws Exception {

        MapperFactoryFactoryBean mapperFactoryFactoryBean = new MapperFactoryFactoryBean();

        MapperFactory mapperFactory = mapperFactoryFactoryBean.getObject();

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        SourceTypeConvert source = new SourceTypeConvert("123","12");

        Dest des = mapperFacade.map(source, Dest.class);

        Assert.assertTrue(des.getName().equals("123"));
        Assert.assertTrue(des.getAge()==2);


    }
    @Mapper(destination = Dest.class)
    public class Source {
        private String name;
        private int age;

        public Source(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @MapperField(fieldName = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


    @Mapper(destination = Dest.class,excludes = {"name"})
    public static class SourceTypeConvert {
        private String name;
        private String age;

        public SourceTypeConvert(String name, String age) {
            this.name = name;
            this.age = age;
        }

        @MapperField(fieldName = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}

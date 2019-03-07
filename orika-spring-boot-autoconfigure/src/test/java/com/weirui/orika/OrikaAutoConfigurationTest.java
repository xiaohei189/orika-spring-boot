package com.weirui.orika;

import ma.glasnost.orika.MapperFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {OrikaAutoConfigurationTest.Config.class})
@RunWith(SpringRunner.class)
public class OrikaAutoConfigurationTest {
    @Autowired
    private MapperFactory mapperFactory;

    @Test
    public void exampleTest() {

        System.out.println("");
    }

    @Configuration
    @ImportAutoConfiguration(OrikaAutoConfiguration.class)
    static class Config {


    }
}

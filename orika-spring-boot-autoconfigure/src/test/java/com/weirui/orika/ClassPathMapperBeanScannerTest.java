package com.weirui.orika;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ClassPathMapperBeanScannerTest {

    @Test
    public void scan() {
        OrikaBeanConfigAttribute configAttribute=null;

        ClassPathMapperBeanScanner classPathMapperBeanScanner = new ClassPathMapperBeanScanner();
        Set<OrikaBeanConfigAttribute> orikaBeanConfigAttributes = classPathMapperBeanScanner.scan("com.weirui.orika");
        for (OrikaBeanConfigAttribute orikaBeanConfigAttribute : orikaBeanConfigAttributes) {
            if (orikaBeanConfigAttribute.getMapper().getName().equals(Source.class.getName())) {
                configAttribute=orikaBeanConfigAttribute;
                break;
            }
        }

        assertNotNull(configAttribute);
        Set<MapperField> mapperFields = configAttribute.getMapperFields();
        System.out.println(mapperFields);
    }

}
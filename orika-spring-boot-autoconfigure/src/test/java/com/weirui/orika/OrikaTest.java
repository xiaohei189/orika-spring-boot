package com.weirui.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrikaTest {

    @Test
    public void givenSrcAndDest_whenMaps_thenCorrect() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Source.class, Dest.class);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        Source src = new Source("Baeldung", 10);
        Dest dest = mapper.map(src, Dest.class);

        assertEquals(dest.getAge(), src.getAge());
        assertEquals(dest.getName(), src.getName());

        Dest dest1 = mapper.map(src, Dest.class);
        assertEquals(dest1.getAge(), src.getAge());
        assertEquals(dest1.getName(), src.getName());
    }

}

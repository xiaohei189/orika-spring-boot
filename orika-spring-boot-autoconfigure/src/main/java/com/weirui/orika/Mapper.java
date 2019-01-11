package com.weirui.orika;

import java.lang.annotation.*;

/**
 * @author 隗锐
 * @dateTime 2019-01-06 13:49:35
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Mapper {

    /**
     * 需要映射的类型
     *
     * @return
     */
    Class<?> destination();

    /*
     * 不映射射字段,默认映射全部
     * */
    String[] excludes() default {};

    /*默认是否映射null到目标类,所有字段有效*/
    boolean mapNull() default false;

    /*反向映射时是否映射null,所有字段有效*/
    boolean mapNullsInReverse() default false;

}

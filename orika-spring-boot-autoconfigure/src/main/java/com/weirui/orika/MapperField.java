package com.weirui.orika;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 隗锐
 * @dateTime 2019-01-06 14:05:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface MapperField {
    /*映射的字段名称,当默认名称不一致时使用*/
    String fieldName();

    /*自定义转换器,在容器中名称,需要将转换器交给ioc容器托管*/
    String converterName() default "";

    /*默认是否映射null到目标类,覆盖类属性*/
    boolean mapNull() default false;

    /*反向映射时是否映射null,覆盖类属性*/
    boolean mapNullsInReverse() default false;

}

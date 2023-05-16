package com.zhouyu.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//@Target()设置注释写的范围
//    ElementType.TYPE   /** Class, interface (including annotation type), or enum declaration */ 只能写在Class interface 或者 enum 上定义
@Target(ElementType.TYPE)
//生效的时间
@Retention(RetentionPolicy.RUNTIME)
public @interface  Scope {
    //默认是创建单例的bean
        String value() default "singleton";
}

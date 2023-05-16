package com.zhouyu.spring_mybatis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//扫描定义的mapper路径
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ZhouyuImportBeanDefinitionRegister.class)
public @interface ZhouyuScan {
    String value() default "";
}

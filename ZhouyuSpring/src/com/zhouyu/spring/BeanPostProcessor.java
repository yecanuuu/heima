package com.zhouyu.spring;
//bean初始化后的后置处理器对象
public interface BeanPostProcessor {

    //初始化前
    public Object postProceessBeforeInitialization(String beanName,Object bean);
    //初始化后
    public Object postProceessAfterInitialization(String beanName,Object bean);
}

package com.zhouyu.service;

import com.zhouyu.spring.BeanPostProcessor;
import com.zhouyu.spring.Component;
import javafx.beans.binding.ObjectExpression;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Component
public class ZhouyuBeanPostProcessor implements BeanPostProcessor{

    //前
    @Override
    public Object postProceessBeforeInitialization(String beanName, Object bean) {
        if(beanName.equals("userService")){
            System.out.println("userService这个类准备开始初始化");
        }
        return bean;
    }
    //后
    //产生的代理对象是一个接口类型的对象
    @Override
    public Object postProceessAfterInitialization(String beanName, Object bean) {
        if(beanName.equals("userService")){
            //利用jdk自带的接口创建代理对象  获取代理对象
//            System.out.println(bean);
//            System.out.println(Arrays.toString(bean.getClass().getInterfaces()));
//            System.out.println(UserInterface.class);
           Object proxyInstance=Proxy.newProxyInstance(ZhouyuBeanPostProcessor.class.getClassLoader(),bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑  ，切面逻辑");
                    return method.invoke(bean,args);
                }
            });
            //返回代理对象
            //返回的类型是UserService实现的UserInterface这个接口类型
            return proxyInstance;
        }
        return bean;
    }
}


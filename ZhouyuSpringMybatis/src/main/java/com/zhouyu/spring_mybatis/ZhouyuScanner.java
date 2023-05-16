package com.zhouyu.spring_mybatis;

import com.zhouyu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;
//定义扫描器   Spring中的扫描器 ClassPathDefinitionScanner
public class ZhouyuScanner extends ClassPathBeanDefinitionScanner {

    //使用spring中的扫描器出苗 registry (Spring 容器)
    public ZhouyuScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        //重写spring中的扫描器  指定扫描接口
        return beanDefinition.getMetadata().isInterface();
    }


//    @Override
//    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
//        return super.isCandidateComponent(beanDefinition);
//    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //获取扫描到的接口
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            //获取到在mapper中的beanDefintion对象
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            //指定构造 方法的参数      getConstructorArgumentValues()获取构造器注入定义的参数  创建ZhouyuFactoryBean的 bean
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            //更改beanDefinition的名字
            beanDefinition.setBeanClassName(ZhouyuFactoryBean.class.getName());
        }
        return beanDefinitionHolders;
    }
}

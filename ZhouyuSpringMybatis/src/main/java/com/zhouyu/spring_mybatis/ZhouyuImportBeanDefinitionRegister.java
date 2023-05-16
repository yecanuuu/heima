package com.zhouyu.spring_mybatis;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;

public class ZhouyuImportBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    @Override                                                                   //registry表示spring 容器
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        //获取ZhouyuScan注解中定义的消息
        MultiValueMap<String, Object> allAnnotationAttributes = importingClassMetadata.getAllAnnotationAttributes(ZhouyuScan.class.getName());
        List<Object> value = allAnnotationAttributes.get("value");
        String path=null;
        for (Object o : value) {
            path=(String)o;
        }
        //doScan中的参数来源
//        System.out.println(path);

        ZhouyuScanner scanner=new ZhouyuScanner(registry);
        scanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });
        //将获取到的path包路径  放到ZhouyuScanner这个类继承自ClassPathBeanDefinitionScanner的方法scan（）中
        // 在scan中将path的参数传递给doScan对包路径进行扫描
        scanner.scan(path);



        /*//将UserMapper加入到springBean容器
        //定义了一个BeanDefinition
        //BeanDefinitionBuilder用于以编程方式创建新的 Spring bean。 它利用了构建器模式。
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        //创建ZhouyuFactoryBean的对象
        beanDefinition.setBeanClass(ZhouyuFactoryBean.class);
        //指定构造 方法的参数      getConstructorArgumentValues()获取构造器注入定义的参数
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
        //将这个beanDefinition对象注册到ConfigcationContext spring bean管理器中   在bean管理器中的名字就是UserMapper
        //registry  就是ApplicationContext
        registry.registerBeanDefinition("userMapper",beanDefinition);

        //将OrderMapper对象加入的Spring中
        //创建BeanDefinition对象
        AbstractBeanDefinition beanDefinition1 = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        //创建ZhouyuFactoryBean的对象
        beanDefinition1.setBeanClass(ZhouyuFactoryBean.class);
        //指定构造 方法的参数
        beanDefinition1.getConstructorArgumentValues().addGenericArgumentValue(OrderMapper.class);
        //将这个beanDefinition对象注册到ConfigcationContext spring bean管理器中   在bean管理器中的名字就是UserMapper
        registry.registerBeanDefinition("orderMapper",beanDefinition1);*/
    }
}

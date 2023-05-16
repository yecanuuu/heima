package com.zhouyu.spring;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ZhouyuApplicationContext {
   private Class configclass;
   //存储被spring管理的bean对象
   private ConcurrentHashMap<String,BeanDefinition> beanDefinitionConcurrentHashMap=new ConcurrentHashMap<>();
   //创建单例池
   private ConcurrentHashMap<String,Object> singletonObjects=new ConcurrentHashMap<>();
   //存放实现了BeanPostProcessor这个接口的方法  (需要执行AOP的方法)
   private ArrayList<BeanPostProcessor> beanPostProcessorsList=new ArrayList<>();

    public ZhouyuApplicationContext(Class configclass) {

        //this.configclass=configclass;
        // 一  扫描 bean

        //判断传过来的配置类上是否有 ConpentScan 注释
        if (configclass.isAnnotationPresent(ComponentScan.class)) {
            //获取配置类上的ComponentScan注解 对象
            ComponentScan componentScanAnnotat =(ComponentScan) configclass.getAnnotation(ComponentScan.class);
            //获取ComponentScan 中的路径值     path:com.zhouyu.service
            String path = componentScanAnnotat.value();
            //将path中的点替换成/
            path=path.replace(".","/");
            //获取ZhuoApplicationContext的类加载器对象  将ZhouyuApplication这个类的加载到JVM中
            ClassLoader classLoader = ZhouyuApplicationContext.class.getClassLoader();
            //路径之间必须要用 /号隔开
//            getClass().getResource(fileName)：表示只会在当前调用类所在的同一路径下查找该fileName文件；
//            getClass().getClassLoader().getResource(fileName)：表示只会在classpath根目录下（/）查找该文件；
            URL resource = classLoader.getResource(path);
            //System.out.println(resource);
           File file=new File(resource.getFile());
            //file:D:\java_ssm\out\production\ZhouyuSpring\com\zhouyu\service
            //System.out.println(file);
           if(file.isDirectory()){
               //获取文件夹下的所有文件
               File[] files = file.listFiles();
               //遍历每个文件
               for (File f : files) {
                   //获取文件的绝对路径
                   String fileName = f.getAbsolutePath();
                   //System.out.println(f);
                   if(fileName.endsWith(".class")){
                        //是以class结尾
                       //判断类是否是一个 bean  判断类上是否有注解Component
                       //loadClass 根据名字获取 这个对象   需要的参数com.zhouyu.service.UserService
                       //将fileName变为com.zhouyu.service.UserService这种形式
                       String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class")).replace("\\",".");
                       //System.out.println(fileName);
                       try {
                           Class<?> Clazz = classLoader.loadClass(className);
                           //判断是否有Component注解
                           if (Clazz.isAnnotationPresent(Component.class)) {
                               //判断这个类是否实现了BeanPostProcessor
                               //isAssignableFrom()判断调用的 class是否是
//                               isAssignableFrom是用来判断子类和父类的关系的，或者接口的实现类和接口的关系的，默认所有的类的终极父类都是Object。
//                               如果A.isAssignableFrom(B)结果是true，证明B可以转换成为A,也就是A可以由B转换而来。 判断A是不是B的父类，或者父接口
                               if(BeanPostProcessor.class.isAssignableFrom(Clazz)){
                                   //调用无参创建对象
                                   BeanPostProcessor instance =(BeanPostProcessor)Clazz.newInstance();
                                   //将对象放到List集合中集中管理
                                  beanPostProcessorsList.add(instance);
                               }

                                //获取需要创建bean的名字
                               Component component = Clazz.getAnnotation(Component.class);
                               String beanName= component.value();
                               //判断 如何Component为""时 默认生成一个名字
                                if(beanName.equals("")){
                                    //默认名字生成器  在decapitaloze中 当前两个字母大写 并且长度大于1 这直接返回
                                    //否则将传的字符串第一个字母小写返回
                                   // System.out.println(Clazz.getSimpleName());
                                    // 1.OrderService
                                    //2.UserService
                                    //3.ZhouyuBeanPostProcessor
                                    //UserService-->userService
                                    beanName= Introspector.decapitalize(Clazz.getSimpleName());
                                    //System.out.println(beanName);
                                }
                               //需要被加载的bean
                               //生成BeanDefinition对象
                               BeanDefinition beanDefinition=new BeanDefinition();
                               beanDefinition.setType(Clazz);
                               //获取这个bean是否是单例的bean 类型Socpe
                               if (Clazz.isAnnotationPresent(Scope.class)) {
                                   //存在Scope注解
                                   Scope scopeAnnotation = Clazz.getAnnotation(Scope.class);
                                   //将设置的Scope值传给beanDefinition
                                   beanDefinition.setSocpe(scopeAnnotation.value());
                               }else{
                                   //Scope默认创建单例的bean
                                   beanDefinition.setSocpe("singleton");
                               }
                                //将扫描到的bean放到Map集合中
                               beanDefinitionConcurrentHashMap.put(beanName,beanDefinition);
                           }
                       } catch (ClassNotFoundException e) {
                           e.printStackTrace();
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (InstantiationException e) {
                           e.printStackTrace();
                       }

                   }
               }
           }

           //二 创建bean  调用createBean()
            for (String beanName: beanDefinitionConcurrentHashMap.keySet()) {
                BeanDefinition beanDefinition = beanDefinitionConcurrentHashMap.get(beanName);
                //判断beanDefinition是否是单例
                if("singleton".equals(beanDefinition.getSocpe())){
                    Object bean = createBean(beanName, beanDefinition);
                    //将创建的对象放到单例池中
                    singletonObjects.put(beanName,bean);
                }else{

                }
            }


        }
    }
    /*
    Bean的生命周期
      1.实例化bean
      2.填充 实例化bean中的需要自动装配的对象
      3.填充其他属性
      4.其他机制（回调，AOP）
      5.添加到单例池（一级缓存）
    */

    //创建bean对象
    private Object createBean(String beanName,BeanDefinition beanDefinition){
        //利用beanDefinition获取对应的bean的Class
        Class clazz = beanDefinition.getType();
        //通过反射创建对象
        //获取无参的构造方法

        try {
            Constructor constructor = clazz.getConstructor();
            //创建对象
            Object instance = constructor.newInstance();
//            Class.newInstance () 只能够调用 无参的构造函数，即默认的构造函数；
//            Constructor.newInstance () 可以根据传入的参数，调用 任意构造构造函数。

//            依赖注入
//            给创建的对象中的有@Autowired的属性进行依赖注入（自动装配）
//            利用反射获取对象的所有属性
            //System.out.println(clazz.getDeclaredFields());
            for (Field declaredField : clazz.getDeclaredFields()) {
                //判断字段上是否存在Autowired注释
                if(declaredField.isAnnotationPresent(Autowired.class)){
                    //开启暴力反射
                   declaredField.setAccessible(true);
                   //进行赋值
                    // set(Object,value)  \
                    // obj：是应该修改其字段的对象，并且
                    //value：这是要修改的obj字段的新值。
                    declaredField.set(instance,getBean(declaredField.getName()));
                }
            }

            //Aware回调机制的实现代码
            //instanceof 是 Java 的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回 boolean 的数据类型
            if(instance instanceof BeanNameAware){
                //间接的调用了某个bean中的回调方法 获取到了该bean在spring容器中的名字
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            //对存放 需要执行AOP方法的对象进行遍历  在初始化前调用
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorsList) {
                instance=beanPostProcessor.postProceessBeforeInitialization(beanName,instance);
            }


            //Spring初始化
            if(instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            //对存放 需要执行AOP方法的对象进行遍历  在初始化后调用
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorsList) {
                instance = beanPostProcessor.postProceessAfterInitialization(beanName, instance);
            }



            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    //通过zhouyuApplicationContext对象调用getBean() 获取对应的bean
    public Object getBean(String beanName){

        //通过beanName到spring容器中寻找bean
        BeanDefinition beanDefinition = beanDefinitionConcurrentHashMap.get(beanName);
        if(beanDefinition==null){
            //当beanDefinition为null 表示传过来的bean不存在或者没有被Spring容器扫描到
            throw new NullPointerException();
        }else{
            //获取Scope 判断是否是单例的形式创建bean
            String socpe = beanDefinition.getSocpe();
            if("singleton".equals(socpe)){
                //是单例的bean
                //如果在单例线程池中存在beanName这个对象就直接在单例池中直接拿
                Object bean= singletonObjects.get(beanName);
                //出现特殊情况，单例池没有创建对应的对象 在单例线程池中不存在beanName这个对象，则使用createBean创建
                if(bean==null){
                    Object o = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,o);
                }

                return bean;

            }else{
                //是多例的bean
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }
        }

    }
}

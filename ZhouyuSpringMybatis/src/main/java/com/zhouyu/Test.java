package com.zhouyu;


import com.zhouyu.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext=
                new AnnotationConfigApplicationContext(AppConfig.class);


        UserService userService = (UserService)annotationConfigApplicationContext.getBean("userService");

        userService.text();

//        //获取到了ZhouyuFactoryBean中的getObject()中产生的代理对象  并且调用了toString（）方法
//        System.out.println(annotationConfigApplicationContext.getBean("zhouyuFactoryBean").toString());
//
//        //获得ZhuoyuFactroyBean 由@Component产生的Bean对象
//        System.out.println(annotationConfigApplicationContext.getBean("&zhouyuFactoryBean").toString());

    }
}

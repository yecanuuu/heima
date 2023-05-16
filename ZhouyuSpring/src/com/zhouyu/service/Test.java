package com.zhouyu.service;

import com.zhouyu.spring.ZhouyuApplicationContext;

public class Test {
    public static void main(String[] args) throws Exception{
        //创建Spring容器    AppConfig为配置类
        ZhouyuApplicationContext zhouyuApplicationContext=new ZhouyuApplicationContext(AppConfig.class);

        UserInterface userService = (UserInterface)zhouyuApplicationContext.getBean("userService");
        userService.test();

    }
}

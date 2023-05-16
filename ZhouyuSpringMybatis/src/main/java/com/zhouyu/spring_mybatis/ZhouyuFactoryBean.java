package com.zhouyu.spring_mybatis;


import com.zhouyu.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ZhouyuFactoryBean implements FactoryBean {

    private SqlSession sqlSession;

    private Class mapperClass; //用来接取 Mapper的对象


    //利用构造方法进行赋值mapperClass进行赋值
    public ZhouyuFactoryBean(Class mapperClass) {
        System.out.println("ZhouyuFactoryBean开始被创建");
        this.mapperClass = mapperClass;
    }

    @Autowired
    public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().addMapper(mapperClass);
        this.sqlSession=sqlSessionFactory.openSession();
    }

    @Override
    public Object getObject() throws Exception {

        //获取由mybatsi获取到的对象userMapper的代理对象
        ;
        return sqlSession.getMapper(mapperClass);
    }




    @Override
    public Class<?> getObjectType() {
        //代理对象的类型
        return mapperClass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

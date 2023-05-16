package com.zhouyu;

import com.zhouyu.spring_mybatis.ZhouyuImportBeanDefinitionRegister;
import com.zhouyu.spring_mybatis.ZhouyuScan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


import java.io.InputStream;


//扫描Mapper
@ComponentScan("com.zhouyu")
@ZhouyuScan("com.zhouyu.mapper")
public class AppConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory()throws Exception{
        InputStream is= Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(is);
        return  sqlSessionFactory;
    }
}

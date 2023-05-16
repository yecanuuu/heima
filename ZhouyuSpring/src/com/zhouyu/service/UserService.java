package com.zhouyu.service;

import com.zhouyu.spring.*;

@Component
@Scope
public class UserService implements UserInterface,InitializingBean,BeanNameAware {
    @Autowired
    private OrderService orderService;
    //用于接收回调函数返回的 这个类在Spring容器的名字
    private String beanName;
    @Override
    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("spring的初始化方法");
    }
}

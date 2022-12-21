package org.example.erzhiri.a06aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * @author erzhiri
 * @Date 2022/12/21
 **/

@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {


    @Override
    public void setBeanName(String name) {
        log.debug("当前 bean:{} 名字叫：{}",this, name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("容器名：{}", applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("当前 bean:{} init", this);
    }

    @Autowired
    public void aaa(ApplicationContext applicationContext) {
        log.debug("当前 bean:{}, 使用 Autowired 容器是：{}", this, applicationContext);
    }

    @PostConstruct
    public void init() {
        log.debug("当前 bean:{} initByPostConstruct", this);
    }
}

package org.example.erzhiri.a06aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author erzhiri
 * @Date 2022/12/21
 **/

@Slf4j
@Configuration
public class MyConfig1 {

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.debug("注入 applicationContext: {}", applicationContext);
    }

    @PostConstruct
    public void init() {
        log.debug("init");
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor1() {
        return beanFactory -> {
            log.debug("执行 beanFactoryPostProcessor1");
        };
    }
}

package org.example.erzhiri.a06aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author erzhiri
 * @Date 2022/12/21
 **/

@Slf4j
@Configuration
public class MyConfig2 implements InitializingBean, ApplicationContextAware {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("init");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("注入 applicationContext: {}", applicationContext);
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor1() {
        return beanFactory -> {
            log.debug("执行 beanFactoryPostProcessor1");
        };
    }
}

package org.example.erzhiri.a03beanlifecycle;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author erzhiri
 * @Date 2022/12/14
 **/

//@Slf4j
@Component
public class LifeCycleBean {

    private static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);


    public LifeCycleBean() {
        log.info("create!");
    }

    @Autowired
    public void autowired(@Value("${JAVA_HOME}") String home) {
        log.info("依赖注入：{}", home);
    }

    @PostConstruct
    public void init() {
        log.info("init");
    }

    @PreDestroy
    public void destroy() {
        log.info("destroy");
    }




}

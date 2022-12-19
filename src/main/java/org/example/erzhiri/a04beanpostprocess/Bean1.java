package org.example.erzhiri.a04beanpostprocess;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author erzhiri
 * @Date 2022/12/18
 **/

@Data
public class Bean1 {

    private static final Logger logger = LoggerFactory.getLogger(Bean1.class);

    private Bean2 bean2;


    @Autowired
    public void setBean2(Bean2 bean2) {
        logger.debug("Autowired 生效 {}", bean2);
        this.bean2 = bean2;
    }


    private Bean3 bean3;

    @Resource
    public void setBean3(Bean3 bean3) {
        logger.debug("Resource 生效{}", bean3);
        this.bean3 = bean3;
    }

    private String home;

    @Autowired
    public void setHome(@Value("JAVA_HOME") String home) {
        logger.debug("Value 生效 {}", home);
        this.home = home;
    }

    @PostConstruct
    public void init() {
        logger.info("PostConstruct 生效");
    }

    @PreDestroy
    public void destroy() {
        logger.info("PreDestroy 生效");
    }


}

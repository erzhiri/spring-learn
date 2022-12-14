package org.example.erzhiri.a03beanlifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author erzhiri
 * @Date 2022/12/14
 **/

@SpringBootApplication
public class LifeCycleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LifeCycleApplication.class, args);
        context.close();
    }
}

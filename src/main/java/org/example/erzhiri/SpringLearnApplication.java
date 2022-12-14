package org.example.erzhiri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author erzhiri
 * @Date 2022/12/13
 **/

@SpringBootApplication
public class SpringLearnApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(SpringLearnApplication.class, args);
    }
}

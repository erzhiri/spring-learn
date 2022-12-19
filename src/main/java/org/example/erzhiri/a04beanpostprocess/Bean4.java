package org.example.erzhiri.a04beanpostprocess;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author erzhiri
 * @Date 2022/12/18
 **/

@Data
@ConfigurationProperties("java")
public class Bean4 {

    private String home;

    private String version;



}

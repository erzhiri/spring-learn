package org.example.erzhiri.a05beanfacotrypostprocessor;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author erzhiri
 * @Date 2022/12/20
 **/
public class A05Application {

    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);

//        // @ComponentScan   @Bean       @Import     @ImportResource
//        context.registerBean(ConfigurationClassPostProcessor.class);
//
//        // MapperScan
//        context.registerBean(MapperScannerConfigurer.class,bd -> {
//            bd.getPropertyValues().add("basePackage", "org.example.erzhiri.a05beanfacotrypostprocessor.mapper");
//        });


        context.registerBean(ComponentScanPostProcessor.class);

        context.refresh();


        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }


        context.close();
    }
}

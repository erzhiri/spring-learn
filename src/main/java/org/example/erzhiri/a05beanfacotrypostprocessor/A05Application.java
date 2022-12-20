package org.example.erzhiri.a05beanfacotrypostprocessor;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
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


        ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        if (null != annotation) {
            String[] basePackages = annotation.basePackages();
            for (String basePackage : basePackages) {
                System.out.println(basePackage);
                // org.example.erzhiri.a05beanfacotrypostprocessor.component -> classpath*:org/example/erzhiri/a05beanfacotrypostprocessor/component/**/*.class
                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                System.out.println(path);
                Resource[] resources = context.getResources(path);
                CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    System.out.println("类：" + metadataReader.getClassMetadata().getClassName());
                    System.out.println("注解：Component - " + metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
                    System.out.println("注解：Component派生 - " + metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
                }
            }
        }

        context.refresh();


        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }


        context.close();
    }
}

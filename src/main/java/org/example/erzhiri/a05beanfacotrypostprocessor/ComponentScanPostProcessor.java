package org.example.erzhiri.a05beanfacotrypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

/**
 * @author long.li02@hand-china.com
 * @Date 2022/12/21 11:01
 **/
public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {

    // context.refesh()
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {

            ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (null != annotation) {
                String[] basePackages = annotation.basePackages();
                for (String basePackage : basePackages) {
                    System.out.println(basePackage);
                    // org.example.erzhiri.a05beanfacotrypostprocessor.component -> classpath*:org/example/erzhiri/a05beanfacotrypostprocessor/component/**/*.class
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    System.out.println(path);
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
                    for (Resource resource : resources) {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
//                        System.out.println("类：" + metadataReader.getClassMetadata().getClassName());
//                        System.out.println("注解：Component - " + metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
//                        System.out.println("注解：Component派生 - " + metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));

                        if (metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()) ||
                                metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                                    .genericBeanDefinition(metadataReader.getClassMetadata().getClassName())
                                    .getBeanDefinition();
                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                                BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
                                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)configurableListableBeanFactory;
                                String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanFactory);
                                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}

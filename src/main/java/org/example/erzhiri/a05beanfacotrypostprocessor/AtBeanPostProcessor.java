package org.example.erzhiri.a05beanfacotrypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author long.li02@hand-china.com
 * @Date 2022/12/21 14:05
 **/
public class AtBeanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        try {
            CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(new ClassPathResource("org/example/erzhiri/a05beanfacotrypostprocessor/Config.class"));
            Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            for (MethodMetadata method : methods) {
                System.out.println(method.getMethodName());
                Map<String, Object> annotationAttributes = method.getAnnotationAttributes(Bean.class.getName());

                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();

                String initMethod = annotationAttributes.get("initMethod").toString();
                if (null != initMethod && initMethod.length() > 0) {
                    beanDefinitionBuilder.setInitMethodName(initMethod);
                }

                beanDefinitionBuilder.setFactoryMethodOnBean(method.getMethodName(), "config");
                beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
                    DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                    beanFactory.registerBeanDefinition(method.getMethodName(), beanDefinition);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

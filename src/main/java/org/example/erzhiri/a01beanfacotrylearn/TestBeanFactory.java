package org.example.erzhiri.a01beanfacotrylearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author erzhiri
 * @Date 2022/12/13
 **/
public class TestBeanFactory {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // bean 的定义(class, scope, init, 销毁)
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        System.out.println("--------------------");

        // 给 beanFactory 添加常用的后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        System.out.println("---------------------");

        // beanFactory 后处理器功能： 补充了一些 Bean 定义
        Collection<BeanFactoryPostProcessor> beanFactoryPostProcessors = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values();
        beanFactoryPostProcessors.forEach(beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory));

        System.out.println("---------------------");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }


//        System.out.println("--------------------");
//        Bean1 bean1 = beanFactory.getBean(Bean1.class);
//        System.out.println(bean1.getBean2());

        // Bean 后处理器，针对 bean 的生命周期提供一些扩展，例如：@AutoWired...  @Resource ...
        Collection<BeanPostProcessor> beanPostProcessors = beanFactory.getBeansOfType(BeanPostProcessor.class).values();
        beanPostProcessors.stream()
                .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> beanFactory.addBeanPostProcessor(beanPostProcessor));

        System.out.println("--------------------");
        Bean1 bean2 = beanFactory.getBean(Bean1.class);
        System.out.println(bean2.getBean2());


        // 预先准备好所有的单例
        beanFactory.preInstantiateSingletons();


        /**
         * a. beanFactory 不会主动去做一些事情
         *      1. 不主动去调用 BeanFactory 后处理器
         *      2. 不主动添加 Bean 后置处理器
         *      3. 不主动初始化单例
         *      4. 不会解析 beanFactory 还不会解析 ${} 和 #{}
         * b. bean 后处理器会有排序的逻辑
         */

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Bean1 beanC = beanFactory.getBean(Bean1.class);
        // 同时存在 AutoWired 和 Resource 按处理器顺序优先
        System.out.println(beanC.getInter());

    }


    @Configuration
    static class Config {

        @Bean
        public Bean1 bean1 (){return new Bean1();}

        @Bean
        public Bean2 bean2() {return new Bean2();};

        @Bean
        public Bean3 bean3() {return new Bean3();};

        @Bean
        public Bean4 bean4() {return new Bean4();};
    }

    interface Inter {

    }

    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        @Autowired
        private Bean2 bean2;
        public Bean1() {
            log.debug("构造 bean1");
        }

        public Bean2 getBean2() {
            return bean2;
        }

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        public Inter getInter() {
            return bean3;
        }

    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.debug("构造 bean2");
        }
    }

    static class Bean3 implements Inter {
        private static final Logger log = LoggerFactory.getLogger(Bean3.class);

        public Bean3() {
            log.debug("构造 bean3");
        }
    }

    static class Bean4 implements Inter {
        private static final Logger log = LoggerFactory.getLogger(Bean4.class);

        public Bean4() {
            log.debug("构造 bean4");
        }
    }
}

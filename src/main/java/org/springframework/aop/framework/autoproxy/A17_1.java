package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author erzhiri
 * @Date 2022/12/29
 **/
public class A17_1 {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean("config", Config.class);
        context.refresh();

        context.close();

        /**
         * 代理创建时机：
         *  a. 初始化之后(无循环依赖)
         *  b. 实例化之后, 依赖注入之前(有循环依赖时), 并暂存于二级缓存
         * 依赖注入与初始化不应被增强，仍应被施加于原始对象
         */
    }




    @Configuration
    static class Config {

        /**
         * 解析 @Aspect、产生代理
         * @return
         */
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }


        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
            return new AutowiredAnnotationBeanPostProcessor();
        }

        /**
         * 解析 @PostConstruct
         * @return
         */
        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }

        @Bean
        public MethodInterceptor advice() {
            return invocation -> {
                System.out.println("before -----");
                Object proceed = invocation.proceed();
                System.out.println("after -----");
                return proceed;
            };
        }

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }


    }

    static class Bean1 {
        public void foo() {};
        public Bean1() {
            System.out.println("Bean1");
        }

        @Autowired
        public void setBean2(Bean2 bean2) {
            System.out.println("bean1 set bean2" + bean2.getClass());
        }

        @PostConstruct
        public void init() {
            System.out.println("bean1 init");
        }
    }

    static class Bean2 {

        public Bean2() {
            System.out.println("Bean2");
        }

        @Autowired
        public void setBean1(Bean1 bean1) {
            System.out.println("bean2 set bean1" + bean1.getClass());
        }

        @PostConstruct
        public void init() {
            System.out.println("bean2 init");
        }
    }
}

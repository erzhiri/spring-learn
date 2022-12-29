package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * @author erzhiri
 * @Date 2022/12/29
 **/
public class A17 {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1", Aspect1.class);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);

        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);



        // BeanPostProcessor
        // 创建 -> (*)依赖注入 -> 初始化(*)
        //
        context.refresh();

        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("################");


        /**
         * 第一个重要的方法：findEligibleAdvisors 找到有【资格】的 Advisors
         *  a. 有【资格】的 Advisors 一部分是低级的，可以自己编写，如下 advisor3
         *  b. 有【资格】的 Advisors 另一部分是高级的， 解析 @AspectJ 后获得
         */
        AnnotationAwareAspectJAutoProxyCreator aspectJAutoProxyCreator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
//        List<Advisor> advisors = aspectJAutoProxyCreator.findEligibleAdvisors(Target1.class, "target1");
        List<Advisor> advisors = aspectJAutoProxyCreator.findEligibleAdvisors(Target2.class, "target2");
        advisors.forEach(System.out::println);
    }





    class Target1 {
        public void foo() {
            System.out.println(" foo ---");
        }
    }

    class Target2 {
        public void bar() {
            System.out.println(" bar ---");
        }
    }

    @Aspect
    static class Aspect1 {

        @Before("execution(* foo())")
        public void before() {
            System.out.println("before foo ---");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("after foo ---");
        }
    }


    @Configuration
    static class Config {

        @Bean
        public Advisor advisor3(MethodInterceptor advice3) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice3);
        }

        @Bean
        public MethodInterceptor advice3() {
            return invocation -> {
                System.out.println("before -----");
                Object proceed = invocation.proceed();
                System.out.println("after -----");
                return proceed;
            };
        }
    }
}

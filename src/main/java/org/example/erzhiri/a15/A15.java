package org.example.erzhiri.a15;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author erzhiri
 * @Date 2022/12/27
 **/
public class A15 {


    public static void main(String[] args) {


        // 1. 准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");

        // 2. 备好通知
        MethodInterceptor advice = methodInvocation -> {
            System.out.println("before -----");
            // 调用目标
            Object proceed = methodInvocation.proceed();
            System.out.println("after ------");
            return proceed;
        };

        // 3. 备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

        /**
         * 4. 创建代理
         *  a. proxyTargetClass = false, 目标实现了接口，用 jdk 代理
         *  b. proxyTargetClass = false, 目标没有实现接口，用 cglib 代理
         *  c. proxyTargetClass = true, 总是使用 cglib 代理
         */
        ProxyFactory factory = new ProxyFactory();

        // 设置目标
        Target1 target1 = new Target1();
        factory.setTarget(target1);

        // 设置切面
        factory.addAdvisor(advisor);

        factory.setInterfaces(target1.getClass().getInterfaces());

        factory.setProxyTargetClass(false);

        I1 proxy = (I1) factory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();
        System.out.println("######################");
        proxy.bar();

        /**
         * 知识点：
         *  1. Spring 的代理实现规则
         *  2. 底层的切点实现
         *  3. 底层的通知实现
         *  4. ProxyFactory 是用来创建代理的核心实现，用 AopProxyFactory 选择具体的代理实现
         *      - JdkDynamicAopProxy
         *      - ObjenesisCglibAopProxy
         */
    }





    interface I1 {
        void foo();
        void bar();
    }


    static class Target1 implements I1{

        @Override
        public void foo() {
            System.out.println("Target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("Target1 bar");
        }
    }

    static class Target2 implements I1{
        @Override
        public void foo() {
            System.out.println("Target2 foo");
        }

        @Override
        public void bar() {
            System.out.println("Target2 bar");
        }
    }
}

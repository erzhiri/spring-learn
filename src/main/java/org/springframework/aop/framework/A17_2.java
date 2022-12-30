package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

/**
 * @author erzhiri
 * @Date 2022/12/29
 **/
public class A17_2 {

    public static void main(String[] args) throws Throwable {




        /**
         * @Before 前置通知会被转为下面原始的 AspectJMethodBeforeAdvice 形式，该对象包含如下信息
         *  a. 通知代码从哪里来
         *  b. 切点是什么(这里为啥要切点，后面解释)
         *  c. 通知对象如何创建，本例共同一个 Aspect 对象
         * 类似的通知还有：
         *  1. AspectJAroundAdvice(环绕通知)
         *  2. AspectJAfterReturningAdvice
         *  3. AspectJAfterThrowingAdvice
         *  4. AspectJAfterAdvice
         */

        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());

        // 高级切面转低级切面类
        List<Advisor> list = new LinkedList<>();

        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                // 解析切点
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut();
                expressionPointcut.setExpression(expression);
                // 通知类
                AbstractAspectJAdvice beforeAdvice = new AspectJMethodBeforeAdvice(method, expressionPointcut, factory);
                // 切面
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(expressionPointcut, beforeAdvice);
                list.add(advisor);
            }

            if (method.isAnnotationPresent(AfterReturning.class)) {
                // 解析切点
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut();
                expressionPointcut.setExpression(expression);
                // 通知类
                AbstractAspectJAdvice beforeAdvice = new AspectJAfterReturningAdvice(method, expressionPointcut, factory);
                // 切面
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(expressionPointcut, beforeAdvice);
                list.add(advisor);
            }

            if (method.isAnnotationPresent(Around.class)) {
                // 解析切点
                String expression = method.getAnnotation(Around.class).value();
                AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut();
                expressionPointcut.setExpression(expression);
                // 通知类
                AbstractAspectJAdvice beforeAdvice = new AspectJAroundAdvice(method, expressionPointcut, factory);
                // 切面
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(expressionPointcut, beforeAdvice);
                list.add(advisor);
            }
        }

        for (Advisor advisor : list) {
            System.out.println(advisor);
        }

        System.out.println("######################################");



        /**
         * 通知统一转化为环绕通知
         * 无论 ProxyFactory 基于那种方式创建代理，最后干活(调用 advice)的是一个 MethodInvocation 对象
         *  a. 因为 advisor 有多个，且一个套一个调用，因此需要一个调用链对象，即 MethodInvocation
         *  b. MethodInvocation 要知道 advice 有哪些，还要知道目标，调用次序如下
         *
         *  将 MethodInvocation 放入了当前线程
         *  | -> before1 ------------------------------------------------   从当前线程获取 invocation
         *  |                                                           |
         *  |   | -> before2 ------------------------------------       |   从当前线程获取 invocation
         *  |   |                                               |       |
         *  |   |   | -> target --------------- 目标 -------  advice2  advice1
         *  |   |                                               |       |
         *  |   | -> after2 ------------------------------------        |
         *  |   |                                                       |
         *  | -> after1 -------------------------------------------------
         *  c. 从上面可以看出，环绕通知才适合作为 advice，因此其他 before、afterReturning 都会被转换成环绕通知
         *  d. 统一转为环绕通知，体现的是设计模式中的适配器模式
         *      - 对外是为了方便使用要区分 before、afterReturning、afterThrowing
         *      - 对内统一都是环绕通知，统一用 MethodInterceptor 表示
         */
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);   // 准备把 MethodInvocation 放入当前线程
        proxyFactory.addAdvisors(list);

        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getDeclaredMethod("foo"), Target.class);
        for (Object o : methodInterceptorList) {
            System.out.println(o);
        }


        /**
         * 创建并执行调用链(环绕通知 + 目标)
         *  次步默一调用过程，是一个简单的地柜过程
         *  1. proceed() 方法调用链中下一个环绕通知
         *  2. 每个环绕如同只内部继续调用 proceed()
         *  3. 调用到没有更多通知了，就调用目标方法
         */
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                null, target, Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList);

        Object proceed = methodInvocation.proceed();


    }


    @org.aspectj.lang.annotation.Aspect
    static class Aspect{

        @Before("execution(* foo())")
        public void before(){
            System.out.println("before1 -----");
        };
        @Before("execution(* foo())")
        public void before2(){
            System.out.println("before2 -----");
        };

        @AfterReturning("execution(* foo())")
        public void afterReturn() {
            System.out.println("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void afterThrowing(Throwable e) {
            System.out.println("afterThrow:" + e.getMessage());
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            System.out.println("around before");
            Object proceed = proceedingJoinPoint.proceed();
            System.out.println("around after");
            return proceed;
        }
    }

    static class Target {
        public void foo() {
            System.out.println("foo");
        }
    }





}

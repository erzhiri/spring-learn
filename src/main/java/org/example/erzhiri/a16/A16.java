package org.example.erzhiri.a16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 *
 * a. 底层切点实现是如何匹配的：调用 aspectJ 的匹配方法
 * b. 比较的关键是实现 MethodMatcher 的 match 方法，用来执行方法的匹配
 *
 * @author erzhiri
 * @Date 2022/12/29
 **/
public class A16 {

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {

        AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
        pointcut1.setExpression("execution(* bar())");
        System.out.println(pointcut1.matches(Target1.class.getMethod("foo"), Target1.class));
        System.out.println(pointcut1.matches(Target1.class.getMethod("bar"), Target1.class));

        AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
        pointcut2.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(pointcut2.matches(Target1.class.getMethod("foo"), Target1.class));
        System.out.println(pointcut2.matches(Target1.class.getMethod("bar"), Target1.class));

        StaticMethodMatcherPointcut pointcut3 = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                // 检查方法上是否标注 Transactional 注解
                MergedAnnotations annotations = MergedAnnotations.from(method);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }

                // 检查类及父类上是否标注 Transactional 注解
                annotations = MergedAnnotations.from(aClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }
                return false;
            }
        };

        System.out.println(pointcut3.matches(Target1.class.getMethod("foo"), Target1.class));
        System.out.println(pointcut3.matches(Target1.class.getMethod("bar"), Target1.class));

        System.out.println(pointcut3.matches(Target2.class.getMethod("foo"), Target2.class));
        System.out.println(pointcut3.matches(Target2.class.getMethod("bar"), Target2.class));
    }






    static class Target1{


        @Transactional
        public void foo() {
            System.out.println("Target1 foo");
        }


        public void bar() {
            System.out.println("Target1 bar");
        }
    }

    static class Target2{


        @Transactional
        public void foo() {
            System.out.println("Target1 foo");
        }


        public void bar() {
            System.out.println("Target1 bar");
        }
    }
}

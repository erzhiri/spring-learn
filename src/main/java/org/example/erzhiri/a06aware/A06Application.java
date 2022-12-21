package org.example.erzhiri.a06aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;

/**
 * @author erzhiri
 * @Date 2022/12/21
 **/


@Slf4j
public class A06Application {

    public static void main(String[] args) {
        /**
         * 1. Aware 接口用于注入一些与容器相关的内容
         *  a. BeanNameAware    注入 bean 的名字
         *  b. BeanFactoryAware 注入 benFactory 对象
         *  c. ApplicationContextAware  注入 applicationContext 容器
         *  d.
         */


        GenericApplicationContext context = new GenericApplicationContext();
//        context.registerBean("myBean", MyBean.class);
//        context.registerBean("myConfig1", MyConfig1.class);
        context.registerBean("myConfig2", MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);




        context.refresh();
        /**
         * 1. 容器中找到所有的 beanFactory 后处理器
         * 2. 添加 bean 后处理器
         * 3. 初始化单例
         */


        context.close();


        /**
         * b, c, d 的功能通过 @Autowired 功能就可以实现，为什么通过 aware 扩展？
         *      a. @Autowired   注解需要用到 bean 后处理器进行扩展
         *      b. Aware    是内置功能，不需要进行扩展，Spring 就可以识别
         *      某些情况下，扩展可能失效，但是内置功能不会失效
         *
         *
         * aware 接口提供了一种内置的注入手段，可以注入 BeanFactory， ApplicationContext
         * initializingBean 接口提供了一种内置的初始化手段
         * 其不受扩展功能影响，总会执行，因此 Spring 框架内部的类常用
         */
    }
}

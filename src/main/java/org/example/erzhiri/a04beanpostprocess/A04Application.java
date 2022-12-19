package org.example.erzhiri.a04beanpostprocess;


import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author erzhiri
 * @Date 2022/12/18
 **/
public class A04Application {

    public static void main(String[] args) {

        // GenericApplicationContext  一个干净的容器，不包含一些 beanFactory 后处理器，bean 后处理器
        GenericApplicationContext context = new GenericApplicationContext();

        // 原始的方法注入三个 bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);


        // 解析 @Autowired @value 注解
        // 依赖注入阶段
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        // 解析 @Resource @PostConstruct @PreDestroy
        // 依赖注入阶段
        // 初始化前
        // 销毁前
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        // 解析 @ConfigurationProperties
        // 初始化前
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());

        // 初始化容器
        // 执行 beanFactory 后处理器方法，添加 bean 后处理器，初始化所有单例
        context.refresh();


        Object bean4 = context.getBean("bean4");
        System.out.println(bean4);


        // 容器销毁
        context.close();

    }
}

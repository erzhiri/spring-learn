package org.example.erzhiri.a04beanpostprocess;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.PropertiesPersister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author erzhiri
 * @Date 2022/12/18
 **/
public class DigInAutowired {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        // ${} 解析器
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders) ;


        // 1. 查找哪些属性、方法添加了 Autowired 称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);


        // 执行依赖注入
//        processor.postProcessProperties(null, bean1, "bean1");
        System.out.println(bean1);


        try {
            Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
            findAutowiringMetadata.setAccessible(true);
            // 获取 bean1 上加了 @Value @Autowired 的成员变量，方法参数信息
            InjectionMetadata injectionMetadata = (InjectionMetadata)findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
            System.out.println(injectionMetadata);

            injectionMetadata.inject(bean1, "bean1", null);
            System.out.println(bean1);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}

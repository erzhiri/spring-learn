package org.example.erzhiri.a03beanlifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author erzhiri
 * @Date 2022/12/15
 **/

@Slf4j
@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {

    /**
     * 销毁前执行
     * 如：@PreDestroy
     * @param bean the bean instance to be destroyed
     * @param beanName the name of the bean
     * @throws BeansException
     */
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<销毁💰执行>>>>>>>>>>>>>>>>");
        }
    }


    /**
     * 实例化之前执行
     * 返回的对象会替换原来的 bean
     * @param beanClass the class of the bean to be instantiated
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<实例化执行>>>>>>>>>>>>>>>>");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }


    /**
     * 实例化之后执行
     * 返回值为 false 时会跳过执行依赖注入阶段
     * @param bean the bean instance created, with properties not having been set yet
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<<<实例化之后执行>>>>>>>>>>>>>>>");
        }
        return true;
    }

    /**
     * 依赖注入阶段执行
     * 如 @Autowired @Value  @Resource
     * @param pvs the property values that the factory is about to apply (never {@code null})
     * @param bean the bean instance created, but whose properties have not yet been set
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<<<依赖注入阶段执行， 如 @Autowired @Value  @Resource>>>>>>>>>>>>>>>");
        }

        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

    /**
     *初始化之前执行
     * 这里返回的对象会替换掉原来的 bean，如@PostConstract， @ConfigurationProperties
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<<<初始化之前执行，这里返回的对象会替换掉原来的 bean，如@PostConstract， @ConfigurationProperties>>>>>>>>>>>>>>>");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("lifeCycleBean")) {
            log.info("<<<<<<<<<<<<<<<初始化之后，这里返回的对象会替换掉原来的 bean，如代理增强>>>>>>>>>>>>>>>");
        }
        return bean;
    }
}

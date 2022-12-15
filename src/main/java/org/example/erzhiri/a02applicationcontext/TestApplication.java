package org.example.erzhiri.a02applicationcontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author erzhiri
 * @Date 2022/12/14
 **/


public class TestApplication {

    private static final Logger log = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();

//        testFileSystemXmlApplicationContext();

//        testAnnotationConfigApplicationContext();

//        testAnnotationConfigServletWebApplicationContext();

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("test1.xml"));
        xmlBeanDefinitionReader.loadBeanDefinitions(new FileSystemResource("src/main/resources/test1.xml"));
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }


    }


    /**
     * 经典的容器，基于 classpath 下的 xml 文件构建容器
     */
    private static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("test1.xml");
        for (String beanDefinitionName : classPathXmlApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        Bean2 bean = classPathXmlApplicationContext.getBean(Bean2.class);
        Bean1 bean1 = bean.getBean1();
        System.out.println(bean);
        System.out.println(bean1);

    }

    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Config.class);
        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }

    private static void testAnnotationConfigServletWebApplicationContext() {
        AnnotationConfigServletWebApplicationContext annotationConfigServletWebApplicationContext = new AnnotationConfigServletWebApplicationContext(WebConfig.class);

        
    }
    
    @Configuration
    static class WebConfig {
        
        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }


        @Bean
        public DispatcherServlet dispatcherServlet() {
             return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean("/hello")
        public Controller controller1() {
            return (httpServletRequest, httpServletResponse) -> {
                httpServletResponse.getWriter().println("hello");
                return null;
            };
        }
    }


    @Configuration
    static class Config {

        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    /**
     * 基于磁盘路径下的 xml 文件进行容器创建
     */
    private static void testFileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext("src/main/resources/test1.xml");
        for (String beanDefinitionName : fileSystemXmlApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        Bean2 bean = (Bean2) fileSystemXmlApplicationContext.getBean("bean2");
        Bean1 bean1 = bean.getBean1();
        System.out.println(bean);
        System.out.println(bean1);
    }


    static class Bean1 {

    }

    static class Bean2 {

        private Bean1 bean1;

        public Bean1 getBean1() {
            return bean1;
        }
        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }
    }

}

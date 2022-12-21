package org.example.erzhiri.a05beanfacotrypostprocessor;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.erzhiri.a05beanfacotrypostprocessor.mapper.TestMapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * @author erzhiri
 * @Date 2022/12/20
 **/

@Configuration
@ComponentScan("org.example.erzhiri.a05beanfacotrypostprocessor.component")
public class Config {

    @Bean
    public Bean1 bean1() {return new Bean1();};


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://1.117.228.239:3306/learn");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("erzhiri");
        return druidDataSource;
    }

    @Bean
    public MapperFactoryBean<TestMapper> mapper1(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<TestMapper> mapperFactoryBean = new MapperFactoryBean<>(TestMapper.class);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactoryBean;
    }

}

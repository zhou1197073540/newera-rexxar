package com.mzkj.usermock.cfg;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.mzkj.usermock.secondMapper", sqlSessionTemplateRef = "db37SqlSessionTemplate")
public class DB37DataSourceConfig {

    @Bean(name = "sencondDataSource")
    @ConfigurationProperties(prefix = "spring.app2") // application.properteis中对应属性的前缀
    public DataSource outData() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "outerSqlSessionFactory")
    public SqlSessionFactory outerSqlSessionFactory(@Qualifier("sencondDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "outerTransactionManager")
    public DataSourceTransactionManager outerTransactionManager(@Qualifier("sencondDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "db37SqlSessionTemplate")
    public SqlSessionTemplate outerSqlSessionTemplate(@Qualifier("outerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

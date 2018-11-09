package com.mouzhiapp.stock_market.config.druid;


import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.mouzhiapp.stock_market.repo.mapper205", sqlSessionTemplateRef = "sqlSessionTemplateow205")
public class OW205DatasourceConfig {

    @Autowired
    Environment env;

    @Bean(name = "ow205", initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean dataSource205() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("ow");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setMinPoolSize(5);
        ds.setMaxPoolSize(20);
        ds.setXaProperties(build("ow"));
        return ds;
    }

    private Properties build(String prefix) {
        Map<String, Object> map = new HashMap();
        Properties prop = new Properties();
        for (Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            String fileName = propertySource.getName();
            if (propertySource instanceof MapPropertySource
                    && fileName.contains("postgre")) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        for (String key : map.keySet()) {
            String[] tmp = key.split("\\.");
            if (prefix.equals(tmp[0])) {
                prop.put(tmp[1], map.get(key));
            }
        }
        return prop;
    }

    @Bean(name = "sqlSessionFactoryow205")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("ow205") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }


    @Bean(name = "sqlSessionTemplateow205")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("sqlSessionFactoryow205") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //手动添加transactionManager，一个就好，注意配置timeout
    @Bean
    public UserTransactionManager hehe() throws Exception {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(2);
        return userTransactionManager;
    }
}

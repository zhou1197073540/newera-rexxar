package com.mouzhiapp.stock_market.config.druid;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


//关键在于把mapper和sqlSession挂钩
@Configuration
@MapperScan(basePackages = "com.mouzhiapp.stock_market.repo.mapper.standBy",
        sqlSessionTemplateRef = "sqlSessionTemplateStockDataStandBy")
public class StockDataStandByDatasourceConfig {

    @Autowired
    Environment env;

    @Bean(name = "stockDataStandBy", initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean dataSource204() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("stockData_standby");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setMinPoolSize(5);
        ds.setMaxPoolSize(20);
        ds.setXaProperties(build("stockData_standBy"));
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

    @Bean(name = "sqlSessionFactoryStockDataStandBy")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("stockDataStandBy") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "sqlSessionTemplateStockDataStandBy")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("sqlSessionFactoryStockDataStandBy") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

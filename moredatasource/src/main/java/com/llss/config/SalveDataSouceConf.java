package com.llss.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.llss.slave.dao"}, sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SalveDataSouceConf {

    @Value("${slave.datasource.url}")
    private String url;

    @Value("${slave.datasource.username}")
    private String user;

    @Value("${slave.datasource.password}")
    private String password;

    @Value("${slave.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "slaveDatasource")
    public DataSource slaveDataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        return ds;
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setDataSource(dataSource);
        return ssfb.getObject();
    }

    @Bean(name = "slaveDataSourceTransactionManager")
    public DataSourceTransactionManager slaveDataSourceTransactionManager(){
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(slaveDataSource());
        return dstm;
    }
}

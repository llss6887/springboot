package com.llss.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.llss.master.dao"}, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSouceConf {

    @Value("${master.datasource.url}")
    private String url;

    @Value("${master.datasource.username}")
    private String user;

    @Value("${master.datasource.password}")
    private String password;

    @Value("${master.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "masterDatasource")
    @Primary
    public DataSource masterDataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        return ds;
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setDataSource(dataSource);
        return ssfb.getObject();
    }

    @Bean(name = "masterDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager masterDataSourceTransactionManager(){
        DataSourceTransactionManager dstm = new DataSourceTransactionManager(masterDataSource());
        return dstm;
    }
}

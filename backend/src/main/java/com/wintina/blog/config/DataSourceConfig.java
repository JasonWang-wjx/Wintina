package com.wintina.blog.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import javax.sql.DataSource;

/**
 * @author Jason
 * @date 2025/12/27
 */

@Configuration
// 指定加载resources/datasource下的jdbc.properties
@PropertySource("classpath:datasource/jdbc.properties")
public class DataSourceConfig {

    // 读取jdbc.properties里的配置项
    @Value("${jdbc.url}")
    private String dbUrl;

    @Value("${jdbc.username}")
    private String dbUsername;

    @Value("${jdbc.password}")
    private String dbPassword;

    @Value("${jdbc.driver-class-name}")
    private String dbDriver;

    // 配置数据源（Spring Boot默认用HikariCP连接池，推荐）
    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dbUrl);
        hikariDataSource.setUsername(dbUsername);
        hikariDataSource.setPassword(dbPassword);
        hikariDataSource.setDriverClassName(dbDriver);
        // 可选：添加连接池参数（如最大连接数）
        hikariDataSource.setMaximumPoolSize(10);
        return hikariDataSource;
    }
}
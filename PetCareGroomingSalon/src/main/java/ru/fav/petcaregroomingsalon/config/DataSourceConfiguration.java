package ru.fav.petcaregroomingsalon.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.util.CustomDataSource;

import javax.sql.DataSource;
import java.util.Properties;


@AllArgsConstructor
public class DataSourceConfiguration {

    private Properties properties;

    public DataSource customDataSource() {

        String url = properties.getProperty("database.url");
        String username =  properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        String driver = properties.getProperty("database.driver");

        return new CustomDataSource(url, username, password, driver);
    }


    public DataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(properties.getProperty("database.url"));
        hikariConfig.setUsername(properties.getProperty("database.username"));
        hikariConfig.setPassword(properties.getProperty("database.password"));
        hikariConfig.setDriverClassName(properties.getProperty("database.driver"));
        hikariConfig.setMaximumPoolSize(
                Integer.parseInt(properties.getProperty("database.hikari.max-pool-size")));

        return new HikariDataSource(hikariConfig);
    }
}

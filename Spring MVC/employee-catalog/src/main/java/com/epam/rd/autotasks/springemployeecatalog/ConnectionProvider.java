package com.epam.rd.autotasks.springemployeecatalog;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

public class ConnectionProvider {
    private static DataSource instance;

    static {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName("org.h2.Driver");
        builder.url("jdbc:h2:mem:testdb");
        builder.username("su");
        builder.password("");
        instance = builder.build();
    }

    public static DataSource getInstance() {
        return instance;
    }
}

package com.rd.epam.autotasks.scopes.config;

import com.rd.epam.autotasks.scopes.config.scopes.JustASecondScope;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JustASecondScopeConfig {
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return factory -> factory.registerScope("justASecond", new JustASecondScope());
    }
}

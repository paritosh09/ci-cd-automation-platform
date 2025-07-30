package com.cicd.automation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.cicd.automation.repository")
@EnableMongoAuditing
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public org.springframework.data.domain.AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    static class AuditorAwareImpl implements org.springframework.data.domain.AuditorAware<String> {

        @Override
        public java.util.Optional<String> getCurrentAuditor() {
            return java.util.Optional.of("system");
        }
    }
}

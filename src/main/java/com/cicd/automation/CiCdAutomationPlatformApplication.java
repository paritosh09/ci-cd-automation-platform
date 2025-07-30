package com.cicd.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class CiCdAutomationPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(CiCdAutomationPlatformApplication.class, args);
    }
}

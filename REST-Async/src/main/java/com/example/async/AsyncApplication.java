package com.example.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//@EnableAsync
@SpringBootApplication(scanBasePackages = "com.example.async")
public class AsyncApplication {

    Logger logger = LoggerFactory.getLogger(AsyncApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

//    @Bean
//    public Executor taskExecutor() {
//        logger.info("Starting taskExecutor");
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(2);
//        threadPoolTaskExecutor.setMaxPoolSize(2);
//        threadPoolTaskExecutor.setQueueCapacity(5);
//        threadPoolTaskExecutor.setThreadNamePrefix("side_chick-");
//        threadPoolTaskExecutor.initialize();
//        return threadPoolTaskExecutor;
//    }
}

package com.sainsburys.productscrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class ProductScrapApplication {

    public static ConfigurableApplicationContext CONTEXT;

    public static void main(String[] args) {

        CONTEXT = SpringApplication.run(ProductScrapApplication.class, args);
        log.warn("Spring Boot is up!");
    }

}

package com.sainsburys.productscrap.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Product Scrapping")
                .description("Product Scrapping for Sainsbury's exercise: https://jsainsburyplc.github.io/serverside-test/")
                .license("All rights reserved to Ofir Germansky Ⓒ")
                .version("1.0.0")
                .contact(new Contact(
                        "Ofir Germansky",
                        "https://ofirg-profile-website.firebaseapp.com/",
                        "ofirg.pro@gmail.com"))
                .build();
    }

    @Bean
    public Docket CurrantsV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.regex("/api/v1.*"))
                .build();
    }

}

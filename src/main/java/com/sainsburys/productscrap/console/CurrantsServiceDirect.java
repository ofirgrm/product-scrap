package com.sainsburys.productscrap.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsburys.productscrap.ProductScrapApplication;
import com.sainsburys.productscrap.common.HtmlDocument;
import com.sainsburys.productscrap.exceptions.ProductPageException;
import com.sainsburys.productscrap.services.v1.CurrantsServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CurrantsServiceDirect {

    private static final String CURRANTS_URL =
            "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk" +
                    "/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    private static final String LOCAL_SPRING_URL = "http://localhost:8080/api/v1/currants";

    private static final CurrantsServiceDirect instance = new CurrantsServiceDirect();

    private ExecutorService executor;

    private CurrantsServiceDirect() {}

    public static CurrantsServiceDirect getInstance() {
        return instance;
    }

    public String callDirectly() {
        HtmlDocument htmlDocument = new HtmlDocument();
        CurrantsServiceImpl productService = new CurrantsServiceImpl(htmlDocument);
        productService.setProductUrl(CURRANTS_URL);
        Object json = productService.getProductList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);
        } catch (JsonProcessingException ex) {
            throw new ProductPageException(ex);
        }
    }

    public Future<Boolean> springBoot(String[] mainArgs) {
        executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            ProductScrapApplication productScrapApplication = new ProductScrapApplication();
            productScrapApplication.main(mainArgs);
            if (mainArgs.length != 1 || !mainArgs[0].equals("quite")) {
                System.out.println("You may call the API using: http://localhost:8080/api/v1/currants");
                System.out.println();
            }

            return true;
        });
    }

    public void springShutdown(String[] mainArgs) {
        System.out.println("Shutdown Spring Boot...");
        ProductScrapApplication.CONTEXT.close();
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted");
        } finally {
            executor.shutdownNow();
        }
        if (mainArgs.length != 1 || !mainArgs[0].equals("quite")) {
            System.out.println("Spring Boot is down!");
            System.out.println();
        }
    }

    public String callHttpRequest() {
        try {
            URL url = new URL(LOCAL_SPRING_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // read the HTTP response
            StringBuffer content = new StringBuffer();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                reader.lines().forEach(content::append);
            }
            // Use jackson to convert JSON to Object and then back
            // to pretty-print JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Object json = objectMapper.readValue(content.toString(), Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(json);

        } catch (IOException ex) {
            throw new ProductPageException(ex);
        }
    }

}

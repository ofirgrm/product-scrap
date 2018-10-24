package com.sainsburys.productscrap.services.v1;

import com.sainsburys.productscrap.common.HtmlDocument;
import com.sainsburys.productscrap.console.CurrantsServiceDirect;
import com.sainsburys.productscrap.exceptions.ProductPageException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Slf4j
public class CurrantsServiceImplIT {

    @Test
    public void testServiceWithSpringBoot() {
        String json = CurrantsServiceDirect.getInstance().callDirectly();
        assertJson(json);
    }

    @Test(expected = ProductPageException.class)
    public void testUrlReaderError() {
        HtmlDocument htmlDocument = new HtmlDocument();
        htmlDocument.readUrl("http://iamapagenotfound");
    }

    @Test
    public void testServiceWithHttpRequest() {
        Future<Boolean> future = CurrantsServiceDirect.getInstance().springBoot(new String[]{"quite"});
        try {
            // wait for Spring Boot to start
            boolean isUp = future.get();
            if (isUp) {
                log.warn("Spring Boot is up");
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new ProductPageException(ex);
        }

        try {
            String json = CurrantsServiceDirect.getInstance().callHttpRequest();
            log.warn("Verifying Results");
            assertJson(json);
        } finally {
            // shut down Spring Boot
            log.warn("Spring Boot is shutting down");
            CurrantsServiceDirect.getInstance().springShutdown(new String[]{"quite"});
        }
    }

    private void assertJson(String json){
        assertNotNull(json);
        assertThat(json.length(), greaterThan(0));
        assertThat(json.indexOf("results"), greaterThan(0));
        assertThat(json.indexOf("title"), greaterThan(0));
        assertThat(json.indexOf("unit_price"), greaterThan(0));
        assertThat(json.indexOf("description"), greaterThan(0));
        assertThat(json.indexOf("kcal_per_100g"), greaterThan(0));
        assertThat(json.indexOf("totals"), greaterThan(0));
        assertThat(json.indexOf("gross"), greaterThan(0));
        assertThat(json.indexOf("vat"), greaterThan(0));
    }

}

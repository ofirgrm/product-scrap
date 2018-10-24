package com.sainsburys.productscrap.services.v1;

import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.console.CurrantsServiceDirect;
import com.sainsburys.productscrap.services.v1.CurrantsService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CurrantsServiceImplSpringIT {

    @Autowired
    CurrantsService currantsService;

    @Test
    public void testServiceWithSpringBoot() {
        ProductResultDTO productResultDTO = currantsService.getProductList();

        assertNotNull(productResultDTO);
        assertNotNull(productResultDTO.getResults());
        assertThat(productResultDTO.getResults().size(), greaterThan(0));
        assertNotNull(productResultDTO.getTotals());
        assertThat(productResultDTO.getTotals().getGross(), greaterThan(0d));
        assertThat(productResultDTO.getTotals().getVat(), greaterThan(0d));
    }

}

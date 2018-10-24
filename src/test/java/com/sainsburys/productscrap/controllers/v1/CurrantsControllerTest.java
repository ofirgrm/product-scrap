package com.sainsburys.productscrap.controllers.v1;

import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.api.v1.model.ProductTotalsDTO;
import com.sainsburys.productscrap.services.v1.CurrantsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class CurrantsControllerTest {

    @Mock
    private CurrantsService currantsService;

    @InjectMocks
    private CurrantsController currantsController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(currantsController).build();
    }

    @Test
    public void getProductList() throws Exception{
        ProductResultDTO productResultDTO = new ProductResultDTO();
        ProductTotalsDTO productTotalsDTO = new ProductTotalsDTO();
        productTotalsDTO.setGross(2.4);
        productTotalsDTO.setVat(0.4);
        productResultDTO.setTotals(productTotalsDTO);

        given(currantsService.getProductList()).willReturn(productResultDTO);

        mockMvc.perform(get("/api/v1/currants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totals.gross", equalTo(2.4)))
                .andExpect(jsonPath("$.totals.vat", equalTo(0.4)));
    }
}
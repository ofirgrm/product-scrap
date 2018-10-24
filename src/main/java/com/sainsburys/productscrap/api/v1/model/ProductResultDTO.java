package com.sainsburys.productscrap.api.v1.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductResultDTO {

    private List<ProductDTO> results;
    private ProductTotalsDTO totals;

}

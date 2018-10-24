package com.sainsburys.productscrap.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductResult {

    private List<Product> products;
    private ProductTotals totals;

}

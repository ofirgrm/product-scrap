package com.sainsburys.productscrap.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductTotals {

    private BigDecimal gross;
    private BigDecimal vat;

}

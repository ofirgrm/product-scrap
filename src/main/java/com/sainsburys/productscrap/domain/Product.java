package com.sainsburys.productscrap.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class Product {

    protected String title;
    protected BigDecimal unitPrice;
    protected String description;

}

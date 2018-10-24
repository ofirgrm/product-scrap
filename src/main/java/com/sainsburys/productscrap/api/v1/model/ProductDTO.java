package com.sainsburys.productscrap.api.v1.model;

import lombok.Data;

@Data
public class ProductDTO {

    private String title;
    private double unit_price;
    private String description;

}

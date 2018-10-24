package com.sainsburys.productscrap.domain;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
public class Currants extends Product {

    private Integer calPer100gr;

    public Currants() {
        this.unitPrice = BigDecimal.ZERO;
    }

    public Currants(String title, String description, BigDecimal unitPrice, int calPer100gr) {
        this.calPer100gr = calPer100gr;
        this.title = title;
        this.unitPrice = unitPrice;
        this.description = description;
    }

}

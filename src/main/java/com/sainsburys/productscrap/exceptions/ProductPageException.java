package com.sainsburys.productscrap.exceptions;


public class ProductPageException extends RuntimeException {

    public ProductPageException(Exception ex) {
        super(ex);
    }

    public ProductPageException(String message) {
        super(message);
    }

    public ProductPageException(String message, Exception ex) {
        super(message, ex);
    }
}

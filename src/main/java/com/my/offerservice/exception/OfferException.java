package com.my.offerservice.exception;


public class OfferException extends RuntimeException {

    private String code;

    public OfferException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

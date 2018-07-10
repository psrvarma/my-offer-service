package com.my.offerservice.model;


import java.math.BigDecimal;
import java.util.Date;

public class Offer {

    private Long id;

    private String description;

    private BigDecimal price;

    private Date validFrom;

    private Date validTill;

    private boolean active;

    private Product product;

}

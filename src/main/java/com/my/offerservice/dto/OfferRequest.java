package com.my.offerservice.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferRequest {

    private Long productId;

    private BigDecimal offerPrice;

    private int offerLengthOfDays;

    private String description;

}

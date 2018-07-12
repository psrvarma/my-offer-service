package com.my.offerservice.util;

import com.my.offerservice.model.Offer;

import java.util.Date;

public class OfferValidator {

    public static boolean isExpired(Offer offer) {
        return offer.getValidTo().after((new Date()));
    }

}

package com.my.offerservice.controller;


import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(value = "offer", method = RequestMethod.POST)
    public OfferRequest createOffer() {

    }

    @RequestMapping(value = "offer/{offerId}", method = RequestMethod.PUT)
    public OfferRequest updateOffer() {

    }

    @RequestMapping(value = "offer/{offerId}", method = RequestMethod.GET)
    public OfferRequest getOffer() {
        return null;
    }

}




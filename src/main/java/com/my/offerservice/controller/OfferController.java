package com.my.offerservice.controller;


import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.dto.OfferResponse;
import com.my.offerservice.exception.ErrorMessage;
import com.my.offerservice.exception.OfferException;
import com.my.offerservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(value = "offer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OfferResponse> createOffer(@RequestBody OfferRequest offerRequest) {
        OfferResponse response = offerService.createOffer(offerRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @RequestMapping(value = "offer/{offerId}", method = RequestMethod.GET)
    public ResponseEntity<OfferResponse> getOffer(@PathVariable(name = "offerId") Long offerId) {
        OfferResponse response = offerService.getOffer(offerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "offer/{offerId}/cancel", method = RequestMethod.PUT)
    public ResponseEntity<OfferResponse> cancelOffer(@PathVariable(name = "offerId") Long offerId) {
        OfferResponse response = offerService.cancelOffer(offerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(OfferException exception) {
        return new ResponseEntity<ErrorMessage>(ErrorMessage.forException(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(Exception exception) {
        return new ResponseEntity<ErrorMessage>(ErrorMessage.forException(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}




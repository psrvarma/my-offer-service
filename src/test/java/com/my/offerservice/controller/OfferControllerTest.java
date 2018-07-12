package com.my.offerservice.controller;


import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.dto.OfferResponse;
import com.my.offerservice.service.OfferService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class OfferControllerTest {

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;


    @Test
    public void shouldCreateOffer() {
        // given
        OfferRequest request = new OfferRequest();

        // when
        ResponseEntity<OfferResponse> responseResponseEntity = offerController.createOffer(request);

        // then
        Assert.assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.OK);
        Mockito.verify(offerService).createOffer(request);
    }

    @Test
    public void shouldCancelOffer() {
        // given
        ResponseEntity<OfferResponse> responseResponseEntity = offerController.cancelOffer(1l);

        // then
        Assert.assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.OK);
        Mockito.verify(offerService).cancelOffer(1l);
    }


    @Test
    public void shouldGetOffer() {
        // given
        ResponseEntity<OfferResponse> responseResponseEntity = offerController.getOffer(1l);

        // then
        Assert.assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.OK);
        Mockito.verify(offerService).getOffer(1l);
    }


}

package com.my.offerservice.service;

import com.my.offerservice.BaseSpringIntegrationTest;
import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.dto.OfferResponse;
import com.my.offerservice.exception.OfferException;
import com.my.offerservice.model.Offer;
import com.my.offerservice.model.Product;
import com.my.offerservice.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;

@ActiveProfiles("test")
public class OfferServiceIntegrationTest extends BaseSpringIntegrationTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
        product.setPrice(new BigDecimal(200));
        product.setName("IPAD");
        productRepository.save(product);
    }

    @Test
    public void shouldCreateAnOFferWhenCreatingTheOfferForTheFirstTime() {
        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");

        // when
        OfferResponse offerResponse = offerService.createOffer(offerRequest);

        // then
        Assert.assertThat(offerResponse.getDescription(), is("TEST"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(500l));
        Assert.assertThat(offerResponse.getId().longValue(), not(equalTo(nullValue())));
    }

    @Test(expected = OfferException.class)
    public void shouldNotCreateAnOfferWhenAnOfferIsActiveAndNotExpired() {
        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        offerService.createOffer(offerRequest);

        // when
        offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        offerService.createOffer(offerRequest);
    }

    @Test(expected = OfferException.class)
    public void shouldThrowExceptionWhenTheAssociatedProductDoesNotExist() {
        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(99l);
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        offerService.createOffer(offerRequest);

        // when
        offerService.createOffer(offerRequest);
    }


    @Test
    @Transactional
    public void shouldCreateAnOfferWhenAnOfferIsNotActiveAndNotExpired() {
        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        OfferResponse offerResponse = offerService.createOffer(offerRequest);
        Assert.assertThat(offerResponse.isCancelled(), is(false));

        Offer offer = productRepository.findById(product.getId()).get().getOffers().get(0);

        offerResponse = offerService.cancelOffer(offer.getId());
        Assert.assertThat(offerResponse.isCancelled(), is(true));

        // when
        offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(600));
        offerRequest.setDescription("TEST2");
        offerResponse = offerService.createOffer(offerRequest);

        // then
        Assert.assertThat(offerResponse.getDescription(), is("TEST2"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(600l));
    }

    @Test
    public void shouldCancelAnOffer() {
        //given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        OfferResponse offerResponse = offerService.createOffer(offerRequest);
        Assert.assertThat(offerResponse.isCancelled(), is(false));

        // when
        offerResponse = offerService.cancelOffer(offerResponse.getId());

        // then
        Assert.assertThat(offerResponse.isCancelled(), is(true));
    }

    @Test
    public void shouldGetAnOffer() {
        //given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST");
        OfferResponse offerResponse = offerService.createOffer(offerRequest);

        // when
        offerResponse = offerService.getOffer(offerResponse.getId());

        // then
        Assert.assertThat(offerResponse.getId(), notNullValue());
        Assert.assertThat(offerResponse.getDescription(), is("TEST"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(500l));

    }




}

package com.my.offerservice.service;

import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.dto.OfferResponse;
import com.my.offerservice.exception.OfferException;
import com.my.offerservice.model.Offer;
import com.my.offerservice.model.Product;
import com.my.offerservice.repository.OfferRepository;
import com.my.offerservice.repository.ProductRepository;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OfferRepository offerRepository;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @Captor
    private ArgumentCaptor<Offer> offerArgumentCaptor;

    @InjectMocks
    private OfferService offerService;

    private Product product;

    @Before
    public void setUp() {
        product = new Product();
        product.setName("IPHONE");
        product.setPrice(new BigDecimal(1000));
        product.setId(1l);
    }

    @Test
    public void shouldCreateAnOfferWhenCreatingTheOfferForTheFirstTime() {
        //given
        OfferRequest request = new OfferRequest();
        request.setDescription("TEST");
        request.setOfferLengthOfDays(100);
        request.setOfferPrice(new BigDecimal(500));
        request.setProductId(product.getId());

        when(productRepository.findById(1l)).thenReturn(Optional.of(product));

        // when
        OfferResponse offerResponse = offerService.createOffer(request);

        // then
        Assert.assertThat(offerResponse.getDescription(), is("TEST"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(500l));

        verify(offerRepository, times(1)).save(offerArgumentCaptor.capture());
        verify(productRepository, times(1)).save(productArgumentCaptor.capture());

        Offer offerArgument = offerArgumentCaptor.getValue();
        Product productArgument = productArgumentCaptor.getValue();

        Assert.assertThat(offerArgument.getDescription(), equalTo("TEST"));
        Assert.assertThat(productArgument.getId(), equalTo(1l));

    }


    @Test(expected = OfferException.class)
    public void shouldNotCreateAnOfferWhenAnOfferIsActiveAndNotExpired() {
        // given
        Offer offer = new Offer();
        offer.setDescription("TEST");
        offer.setActive(true);
        offer.setValidTo(DateUtils.addDays(new Date(), 5));
        offer.setDescription("TEST");
        product.getOffers().add(offer);

        when(productRepository.findById(1l)).thenReturn(Optional.of(product));

        // when
        OfferRequest offerRequest = new OfferRequest();
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

        // when
        offerService.createOffer(offerRequest);
    }

    @Test
    public void shouldCreateAnOfferWhenAnOfferIsNotActiveAndIsNotExpired() {
        // given
        Offer offer = new Offer();
        offer.setId(1l);
        offer.setDescription("TEST");
        offer.setActive(false);
        offer.setValidTo(DateUtils.addDays(new Date(), 5));
        product.getOffers().add(offer);

        when(productRepository.findById(1l)).thenReturn(Optional.of(product));

        // when
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(product.getId());
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(500));
        offerRequest.setDescription("TEST1");

        OfferResponse offerResponse = offerService.createOffer(offerRequest);

        //then
        Assert.assertThat(offerResponse.getDescription(), is("TEST1"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(500l));

        verify(offerRepository, times(1)).save(offerArgumentCaptor.capture());
        verify(productRepository, times(1)).save(productArgumentCaptor.capture());

        Offer offerArgument = offerArgumentCaptor.getValue();
        Product productArgument = productArgumentCaptor.getValue();

        Assert.assertThat(offerArgument.getDescription(), equalTo("TEST1"));
        Assert.assertThat(productArgument.getId(), equalTo(1l));
    }

    @Test
    public void shouldGetAndOffer() {
        // given
        Offer offer = new Offer();
        offer.setDescription("TEST");
        offer.setActive(false);
        offer.setPrice(new BigDecimal(600));
        offer.setValidTo(DateUtils.addDays(new Date(), 5));
        offer.setId(1l);

        when(offerRepository.findById(1l)).thenReturn(Optional.of(offer));

        // when
        OfferResponse offerResponse = offerService.getOffer(1l);

        //then
        Assert.assertThat(offerResponse.getDescription(), is("TEST"));
        Assert.assertThat(offerResponse.getPrice().longValue(), equalTo(600l));
        verify(offerRepository, times(1)).findById(1l);
    }

    @Test
    public void shouldCancelAnOffer() {
        // given
        Offer offer = new Offer();
        offer.setDescription("TEST");
        offer.setActive(false);
        offer.setValidTo(DateUtils.addDays(new Date(), 5));
        offer.setId(1l);

        when(offerRepository.findById(1l)).thenReturn(Optional.of(offer));

        // when
        OfferResponse offerResponse = offerService.cancelOffer(1l);

        //then
        Assert.assertThat(offerResponse.isCancelled(), is(true));
        verify(offerRepository, times(1)).findById(1l);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

}

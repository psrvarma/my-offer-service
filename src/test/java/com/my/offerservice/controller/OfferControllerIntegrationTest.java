package com.my.offerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.offerservice.BaseMvcIntegrationTest;
import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.model.Product;
import com.my.offerservice.repository.OfferRepository;
import com.my.offerservice.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class OfferControllerIntegrationTest extends BaseMvcIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    @Transactional
    public void shouldCreateOfferForValidData() throws Exception {
        //given
        Long id = productRepository.findAll().get(0).getId();

        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(id);
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(100));
        offerRequest.setDescription("Christmas Offer");

        // when
        mockMvc.perform(post("/merchant/offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.cancelled", equalTo(false)))
                .andExpect(jsonPath("$.expired", equalTo(false)))
                .andExpect(jsonPath("$.description", equalTo("Christmas Offer")))
                .andExpect(status().isOk());

        // then
        Assert.assertThat(productRepository.findAll().size(), is(1));
        Assert.assertThat(productRepository.findAll().get(0).getOffers().size(), is(1));
    }

    @Test
    @Transactional
    public void shouldCancelOffer() throws Exception {
        // given
        createOffer();

        Long id =  productRepository.findAll().get(0).getOffers().get(0).getId();

        // when-then
        mockMvc.perform(put("/merchant/offer/" + id + "/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.cancelled", equalTo(true)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void shouldGetOffer() throws Exception {
        // given
        createOffer();

        Long id =  productRepository.findAll().get(0).getOffers().get(0).getId();

        // when-then
        mockMvc.perform(get("/merchant/offer/" + id))
                .andExpect(jsonPath("$.cancelled", equalTo(false)))
                .andExpect(jsonPath("$.expired", equalTo(false)))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.description", equalTo("Christmas Offer")))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldThrowProductNotFoundException() throws Exception {
        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(99l);
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(100));
        offerRequest.setDescription("Christmas OFfer");

        // when-then
        mockMvc.perform(post("/merchant/offer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)))
                .andExpect(jsonPath("$.code", equalTo("product_does_not_exist")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowActiveOfferExistsException() throws Exception {
        //given
        createOffer();

        Long id = productRepository.findAll().get(0).getId();

        // given
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(id);
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(100));
        offerRequest.setDescription("Christmas OFfer");

        // when-then
        mockMvc.perform(post("/merchant/offer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)))
                .andExpect(jsonPath("$.code", equalTo("active_non_expired_offer_exists")))
                .andExpect(status().isBadRequest());
    }


    private void createOffer() throws Exception {
        Long id = productRepository.findAll().get(0).getId();

        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setProductId(id);
        offerRequest.setOfferLengthOfDays(100);
        offerRequest.setOfferPrice(new BigDecimal(100));
        offerRequest.setDescription("Christmas Offer");

        // when-then
        mockMvc.perform(post("/merchant/offer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)))
                .andExpect(status().isOk());
    }



}

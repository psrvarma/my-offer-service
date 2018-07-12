package com.my.offerservice;

import com.my.offerservice.repository.OfferRepository;
import com.my.offerservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class})
@Slf4j
public abstract class BaseSpringIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OfferRepository offerRepository;

    @After
    public void tearDown() {
        productRepository.deleteAll();
        offerRepository.deleteAll();
    }

}

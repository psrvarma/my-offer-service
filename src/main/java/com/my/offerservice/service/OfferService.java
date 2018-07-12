package com.my.offerservice.service;

import com.my.offerservice.dto.OfferRequest;
import com.my.offerservice.dto.OfferResponse;
import com.my.offerservice.exception.OfferException;
import com.my.offerservice.model.Offer;
import com.my.offerservice.model.Product;
import com.my.offerservice.repository.OfferRepository;
import com.my.offerservice.repository.ProductRepository;
import com.my.offerservice.util.OfferValidator;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    private ProductRepository productRepository;

    private OfferRepository offerRepository;

    @Autowired
    public OfferService(ProductRepository productRepositor, OfferRepository offerRepository) {
        this.productRepository = productRepositor;
        this.offerRepository = offerRepository;
    }

    @Transactional
    public OfferResponse createOffer(OfferRequest offerRequest) {
        Optional<Product> optionalProduct = productRepository.findById(offerRequest.getProductId());
        Offer offer;
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (!currentlyAnyOfferIsActiveAndNotExpired(product.getOffers())) {
                offer = new Offer();
                offer.setActive(true);
                offer.setDescription(offerRequest.getDescription());
                offer.setPrice(offerRequest.getOfferPrice());

                Date validToDate = DateUtils.addDays(new Date(), offerRequest.getOfferLengthOfDays());
                offer.setValidTo(validToDate);
                offerRepository.save(offer);

                product.getOffers().add(offer);
                productRepository.save(product);
            } else {
                throw new OfferException("active_non_expired_offer_exists", "Currently An Offer Is Active and Not Expired");
            }
        } else {
            throw new OfferException("product_does_not_exist", "Product not available to associate offer with");
        }

        return OfferResponse.transform(offer);
    }

    @Transactional
    public OfferResponse cancelOffer(Long offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        Offer offer = null;
        if (offerOptional.isPresent()) {
            offer = offerOptional.get();
            offer.setActive(false);
            offerRepository.save(offer);
        }
        return OfferResponse.transform(offer);
    }

    @Transactional
    public OfferResponse getOffer(Long offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        Offer offer = null;
        if (offerOptional.isPresent()) {
            offer = offerOptional.get();
        }
        return OfferResponse.transform(offer);
    }

    private boolean currentlyAnyOfferIsActiveAndNotExpired(List<Offer> offerList) {
        boolean currentlyActive = false;
        for (Offer offer : offerList) {
            if (offer.isActive() && OfferValidator.isExpired(offer)) {
                currentlyActive = true;
            }
        }
        return currentlyActive;
    }


}

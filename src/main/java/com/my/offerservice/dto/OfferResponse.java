package com.my.offerservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.offerservice.model.Offer;
import com.my.offerservice.util.OfferValidator;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {

    private Long id;

    private String description;

    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date validTill;

    private boolean cancelled;

    private boolean expired;


    public static OfferResponse transform(Offer offer) {
        OfferResponseBuilder builder = OfferResponse.builder()
                .cancelled(!offer.isActive())
                .description(offer.getDescription())
                .price(offer.getPrice())
                .id(offer.getId())
                .expired(!OfferValidator.isExpired(offer));
        if (!OfferValidator.isExpired(offer)) {
            builder.validTill(offer.getValidTo());
        }
        return builder.build();
    }

}

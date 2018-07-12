package com.my.offerservice.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Product")
@Table(name = "PRODUCT")
public class Product {

    private Long id;

    private String name;

    private BigDecimal price;

    private List<Offer> offers;

    @Id
    @Column(name = "ID")
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PRICE")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @OneToMany(
            targetEntity = Offer.class,
            orphanRemoval = true,
            cascade = {CascadeType.ALL}
    )
    public List<Offer> getOffers() {
        if (offers == null) {
            offers = new ArrayList<>();
        }
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}

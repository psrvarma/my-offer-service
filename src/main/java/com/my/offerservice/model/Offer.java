package com.my.offerservice.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "Offer")
@Table(name = "OFFER")
public class Offer {

    private Long id;

    private String description;

    private BigDecimal price;

    private Date validTo;

    private boolean active;

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

    @Column(name = "DESC")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PRICE")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "VALID_TO")
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Column(name = "ACTIVE")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

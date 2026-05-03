package com.jitendra.marketplace.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 1200)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(length = 120)
    private String supplierEmail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    protected Product() {
    }

    public Product(String name, String description, BigDecimal price, String category, String supplierEmail, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.supplierEmail = supplierEmail;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void updateDetails(String name, String description, BigDecimal price, String category, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = status;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}

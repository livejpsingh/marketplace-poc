package com.jitendra.marketplace.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String status,
        String supplierEmail,
        Instant createdAt,
        Instant updatedAt
) {
}

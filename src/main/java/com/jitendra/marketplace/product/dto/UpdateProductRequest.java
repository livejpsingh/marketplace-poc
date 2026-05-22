package com.jitendra.marketplace.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 1200) String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @NotBlank @Size(max = 80) String category,
        @NotBlank @Pattern(regexp = "DRAFT|PUBLISHED|INACTIVE", message = "status must be DRAFT, PUBLISHED or INACTIVE") String status
) {
}

package com.example.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Name is required")
        String name,
        @NotNull(message = "Description is required")
        String description,
        @Positive(message = "Quantity should be positive")
        Double availableQuantity,
        @Positive(message = "Quantity should be positive")
        BigDecimal price,
        @NotNull(message = "CategoryId is required")
        Integer categoryId
) {
}

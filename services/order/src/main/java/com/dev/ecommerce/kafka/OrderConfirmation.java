package com.dev.ecommerce.kafka;

import com.dev.ecommerce.customer.CustomerResponse;
import com.dev.ecommerce.order.PaymentMethod;
import com.dev.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}

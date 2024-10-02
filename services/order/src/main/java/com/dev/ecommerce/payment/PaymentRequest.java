package com.dev.ecommerce.payment;

import com.dev.ecommerce.customer.CustomerResponse;
import com.dev.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}

package com.dev.ecommerce.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${application.config.product-url}"
)
public interface IProductClient {
    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<PurchaseResponse> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);
}

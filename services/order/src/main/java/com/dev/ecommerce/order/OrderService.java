package com.dev.ecommerce.order;

import com.dev.ecommerce.customer.CustomerClient;
import com.dev.ecommerce.exception.BusinessException;
import com.dev.ecommerce.kafka.OrderConfirmation;
import com.dev.ecommerce.kafka.OrderProducer;
import com.dev.ecommerce.orderline.OrderLineRequest;
import com.dev.ecommerce.orderline.OrderLineService;
import com.dev.ecommerce.payment.PaymentClient;
import com.dev.ecommerce.payment.PaymentRequest;
import com.dev.ecommerce.product.IProductClient;
import com.dev.ecommerce.product.ProductClient;
import com.dev.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    private final CustomerClient customerClient; // Using FeignClient
    //private final ProductClient productClient; // Using RestTemplate
    private final IProductClient productClient;

    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    private final OrderProducer orderProducer;// kafka

    private final PaymentClient paymentClient; //Feign to product-service

    public Integer createOrder(OrderRequest request) {
        // check the customer --> OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()->new BusinessException("Cannot create order with customer ID: "+request.customerId()));

        // purchase the products --> product-ms (RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        // persist order
        var order = this.repository.save(mapper.fromOrderRequestToOrder(request));

        // persist order lines
        for (PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
              new OrderLineRequest(
                      null,
                      order.getId(),
                      purchaseRequest.productId(),
                      purchaseRequest.quantity()
              )
            );
        }

        // start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest); // All to send paymentnotification(using feign)

        // send the order confirmation --> notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrderToOrderResponse)
                .collect(Collectors.toList());//mutable
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrderToOrderResponse)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("There is no order with ID: %s", orderId)
                ));
    }
}

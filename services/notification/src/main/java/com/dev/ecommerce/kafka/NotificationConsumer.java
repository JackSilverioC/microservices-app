package com.dev.ecommerce.kafka;

import com.dev.ecommerce.email.EmailService;
import com.dev.ecommerce.kafka.order.OrderConfirmation;
import com.dev.ecommerce.kafka.payment.PaymentConfirmation;
import com.dev.ecommerce.notification.Notification;
import com.dev.ecommerce.notification.NotificationRepository;
import com.dev.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    // PaymentConfirmation
    @KafkaListener(topics = "payment-topic", groupId = "paymentGroup")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from payment-topic: %s", paymentConfirmation));
        repository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );

        // send email
        var customerName  = paymentConfirmation.customerFirstName()+" "+paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }
    // OrderConfirmation
    @KafkaListener(topics = "order-topic", groupId = "orderGroup")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(String.format("Consuming the message from order-topic: %s", orderConfirmation));
        repository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        // send email
        var customerName  = orderConfirmation.customer().firstname()+" "+orderConfirmation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );

    }
}

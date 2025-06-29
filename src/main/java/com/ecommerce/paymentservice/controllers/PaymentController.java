package com.ecommerce.paymentservice.controllers;

import com.ecommerce.paymentservice.services.IPaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    public String generatePaymentLink(@PathVariable("orderId") String orderId) {
        try {
            String paymentLink = paymentService.generatePaymentLink(orderId);
            return "Payment link generated successfully: " + paymentLink;
        } catch (Exception e) {
            return "Error generating payment link: " + e.getMessage();
        }
    }
}

package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.model.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")

public class PaymentController {
    private final Payment payment = new Payment(1L, 23.2);

    private final Map<Long, Payment> paymentStorage = new HashMap<>();
    public  PaymentController() {
        paymentStorage.put(1L, new Payment(1L, 23.2));
        paymentStorage.put(2L, new Payment(2L, 45.7));
        paymentStorage.put(3L, new Payment(3L, 32.4));
        paymentStorage.put(4L, new Payment(4L, 89.0));
        paymentStorage.put(5L, new Payment(5L, 87.1));
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentStorage.get(id);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentStorage.values());
    }
}


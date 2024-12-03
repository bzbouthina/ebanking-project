package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CardPaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CardPayment getCardPaymentSample1() {
        return new CardPayment().id(1L).cardNumber("cardNumber1").cardHolderName("cardHolderName1").cvv("cvv1").description("description1");
    }

    public static CardPayment getCardPaymentSample2() {
        return new CardPayment().id(2L).cardNumber("cardNumber2").cardHolderName("cardHolderName2").cvv("cvv2").description("description2");
    }

    public static CardPayment getCardPaymentRandomSampleGenerator() {
        return new CardPayment()
            .id(longCount.incrementAndGet())
            .cardNumber(UUID.randomUUID().toString())
            .cardHolderName(UUID.randomUUID().toString())
            .cvv(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}

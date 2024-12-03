package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QRPaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QRPayment getQRPaymentSample1() {
        return new QRPayment().id(1L).qrCode("qrCode1").description("description1");
    }

    public static QRPayment getQRPaymentSample2() {
        return new QRPayment().id(2L).qrCode("qrCode2").description("description2");
    }

    public static QRPayment getQRPaymentRandomSampleGenerator() {
        return new QRPayment()
            .id(longCount.incrementAndGet())
            .qrCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}

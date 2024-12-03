package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BillPaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BillPayment getBillPaymentSample1() {
        return new BillPayment().id(1L).billReference("billReference1").billIssuer("billIssuer1").description("description1");
    }

    public static BillPayment getBillPaymentSample2() {
        return new BillPayment().id(2L).billReference("billReference2").billIssuer("billIssuer2").description("description2");
    }

    public static BillPayment getBillPaymentRandomSampleGenerator() {
        return new BillPayment()
            .id(longCount.incrementAndGet())
            .billReference(UUID.randomUUID().toString())
            .billIssuer(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}

package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .login("login1")
            .fullName("fullName1")
            .motherName("motherName1")
            .nationalId("nationalId1")
            .phoneNumber("phoneNumber1")
            .email("email1")
            .addressLine1("addressLine11");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .login("login2")
            .fullName("fullName2")
            .motherName("motherName2")
            .nationalId("nationalId2")
            .phoneNumber("phoneNumber2")
            .email("email2")
            .addressLine1("addressLine12");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .motherName(UUID.randomUUID().toString())
            .nationalId(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString());
    }
}

package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BankAccount getBankAccountSample1() {
        return new BankAccount()
            .id(1L)
            .login("login1")
            .accountNumber("accountNumber1")
            .iban("iban1")
            .currency("currency1")
            .branch("branch1");
    }

    public static BankAccount getBankAccountSample2() {
        return new BankAccount()
            .id(2L)
            .login("login2")
            .accountNumber("accountNumber2")
            .iban("iban2")
            .currency("currency2")
            .branch("branch2");
    }

    public static BankAccount getBankAccountRandomSampleGenerator() {
        return new BankAccount()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .accountNumber(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .branch(UUID.randomUUID().toString());
    }
}

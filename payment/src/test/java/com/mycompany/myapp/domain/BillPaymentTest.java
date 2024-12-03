package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BillPaymentTestSamples.*;
import static com.mycompany.myapp.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillPayment.class);
        BillPayment billPayment1 = getBillPaymentSample1();
        BillPayment billPayment2 = new BillPayment();
        assertThat(billPayment1).isNotEqualTo(billPayment2);

        billPayment2.setId(billPayment1.getId());
        assertThat(billPayment1).isEqualTo(billPayment2);

        billPayment2 = getBillPaymentSample2();
        assertThat(billPayment1).isNotEqualTo(billPayment2);
    }

    @Test
    void paymentTest() {
        BillPayment billPayment = getBillPaymentRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        billPayment.setPayment(paymentBack);
        assertThat(billPayment.getPayment()).isEqualTo(paymentBack);

        billPayment.payment(null);
        assertThat(billPayment.getPayment()).isNull();
    }
}

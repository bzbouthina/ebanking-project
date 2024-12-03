package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BillPaymentTestSamples.*;
import static com.mycompany.myapp.domain.CardPaymentTestSamples.*;
import static com.mycompany.myapp.domain.PaymentTestSamples.*;
import static com.mycompany.myapp.domain.QRPaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void qRPaymentTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        QRPayment qRPaymentBack = getQRPaymentRandomSampleGenerator();

        payment.addQRPayment(qRPaymentBack);
        assertThat(payment.getQRPayments()).containsOnly(qRPaymentBack);
        assertThat(qRPaymentBack.getPayment()).isEqualTo(payment);

        payment.removeQRPayment(qRPaymentBack);
        assertThat(payment.getQRPayments()).doesNotContain(qRPaymentBack);
        assertThat(qRPaymentBack.getPayment()).isNull();

        payment.qRPayments(new HashSet<>(Set.of(qRPaymentBack)));
        assertThat(payment.getQRPayments()).containsOnly(qRPaymentBack);
        assertThat(qRPaymentBack.getPayment()).isEqualTo(payment);

        payment.setQRPayments(new HashSet<>());
        assertThat(payment.getQRPayments()).doesNotContain(qRPaymentBack);
        assertThat(qRPaymentBack.getPayment()).isNull();
    }

    @Test
    void cardPaymentTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        CardPayment cardPaymentBack = getCardPaymentRandomSampleGenerator();

        payment.addCardPayment(cardPaymentBack);
        assertThat(payment.getCardPayments()).containsOnly(cardPaymentBack);
        assertThat(cardPaymentBack.getPayment()).isEqualTo(payment);

        payment.removeCardPayment(cardPaymentBack);
        assertThat(payment.getCardPayments()).doesNotContain(cardPaymentBack);
        assertThat(cardPaymentBack.getPayment()).isNull();

        payment.cardPayments(new HashSet<>(Set.of(cardPaymentBack)));
        assertThat(payment.getCardPayments()).containsOnly(cardPaymentBack);
        assertThat(cardPaymentBack.getPayment()).isEqualTo(payment);

        payment.setCardPayments(new HashSet<>());
        assertThat(payment.getCardPayments()).doesNotContain(cardPaymentBack);
        assertThat(cardPaymentBack.getPayment()).isNull();
    }

    @Test
    void billPaymentTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        BillPayment billPaymentBack = getBillPaymentRandomSampleGenerator();

        payment.addBillPayment(billPaymentBack);
        assertThat(payment.getBillPayments()).containsOnly(billPaymentBack);
        assertThat(billPaymentBack.getPayment()).isEqualTo(payment);

        payment.removeBillPayment(billPaymentBack);
        assertThat(payment.getBillPayments()).doesNotContain(billPaymentBack);
        assertThat(billPaymentBack.getPayment()).isNull();

        payment.billPayments(new HashSet<>(Set.of(billPaymentBack)));
        assertThat(payment.getBillPayments()).containsOnly(billPaymentBack);
        assertThat(billPaymentBack.getPayment()).isEqualTo(payment);

        payment.setBillPayments(new HashSet<>());
        assertThat(payment.getBillPayments()).doesNotContain(billPaymentBack);
        assertThat(billPaymentBack.getPayment()).isNull();
    }
}

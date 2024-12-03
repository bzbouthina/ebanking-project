package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PaymentTestSamples.*;
import static com.mycompany.myapp.domain.QRPaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QRPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QRPayment.class);
        QRPayment qRPayment1 = getQRPaymentSample1();
        QRPayment qRPayment2 = new QRPayment();
        assertThat(qRPayment1).isNotEqualTo(qRPayment2);

        qRPayment2.setId(qRPayment1.getId());
        assertThat(qRPayment1).isEqualTo(qRPayment2);

        qRPayment2 = getQRPaymentSample2();
        assertThat(qRPayment1).isNotEqualTo(qRPayment2);
    }

    @Test
    void paymentTest() {
        QRPayment qRPayment = getQRPaymentRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        qRPayment.setPayment(paymentBack);
        assertThat(qRPayment.getPayment()).isEqualTo(paymentBack);

        qRPayment.payment(null);
        assertThat(qRPayment.getPayment()).isNull();
    }
}

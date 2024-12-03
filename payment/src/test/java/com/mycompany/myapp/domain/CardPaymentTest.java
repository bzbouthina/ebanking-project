package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CardPaymentTestSamples.*;
import static com.mycompany.myapp.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardPayment.class);
        CardPayment cardPayment1 = getCardPaymentSample1();
        CardPayment cardPayment2 = new CardPayment();
        assertThat(cardPayment1).isNotEqualTo(cardPayment2);

        cardPayment2.setId(cardPayment1.getId());
        assertThat(cardPayment1).isEqualTo(cardPayment2);

        cardPayment2 = getCardPaymentSample2();
        assertThat(cardPayment1).isNotEqualTo(cardPayment2);
    }

    @Test
    void paymentTest() {
        CardPayment cardPayment = getCardPaymentRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        cardPayment.setPayment(paymentBack);
        assertThat(cardPayment.getPayment()).isEqualTo(paymentBack);

        cardPayment.payment(null);
        assertThat(cardPayment.getPayment()).isNull();
    }
}

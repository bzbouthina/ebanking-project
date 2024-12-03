package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BankAccountTestSamples.*;
import static com.mycompany.myapp.domain.InternatonalTransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InternatonalTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternatonalTransfer.class);
        InternatonalTransfer internatonalTransfer1 = getInternatonalTransferSample1();
        InternatonalTransfer internatonalTransfer2 = new InternatonalTransfer();
        assertThat(internatonalTransfer1).isNotEqualTo(internatonalTransfer2);

        internatonalTransfer2.setId(internatonalTransfer1.getId());
        assertThat(internatonalTransfer1).isEqualTo(internatonalTransfer2);

        internatonalTransfer2 = getInternatonalTransferSample2();
        assertThat(internatonalTransfer1).isNotEqualTo(internatonalTransfer2);
    }

    @Test
    void bankAccountTest() {
        InternatonalTransfer internatonalTransfer = getInternatonalTransferRandomSampleGenerator();
        BankAccount bankAccountBack = getBankAccountRandomSampleGenerator();

        internatonalTransfer.setBankAccount(bankAccountBack);
        assertThat(internatonalTransfer.getBankAccount()).isEqualTo(bankAccountBack);

        internatonalTransfer.bankAccount(null);
        assertThat(internatonalTransfer.getBankAccount()).isNull();
    }
}

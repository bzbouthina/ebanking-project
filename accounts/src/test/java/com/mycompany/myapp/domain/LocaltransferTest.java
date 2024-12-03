package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BankAccountTestSamples.*;
import static com.mycompany.myapp.domain.LocaltransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocaltransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Localtransfer.class);
        Localtransfer localtransfer1 = getLocaltransferSample1();
        Localtransfer localtransfer2 = new Localtransfer();
        assertThat(localtransfer1).isNotEqualTo(localtransfer2);

        localtransfer2.setId(localtransfer1.getId());
        assertThat(localtransfer1).isEqualTo(localtransfer2);

        localtransfer2 = getLocaltransferSample2();
        assertThat(localtransfer1).isNotEqualTo(localtransfer2);
    }

    @Test
    void bankAccountTest() {
        Localtransfer localtransfer = getLocaltransferRandomSampleGenerator();
        BankAccount bankAccountBack = getBankAccountRandomSampleGenerator();

        localtransfer.setBankAccount(bankAccountBack);
        assertThat(localtransfer.getBankAccount()).isEqualTo(bankAccountBack);

        localtransfer.bankAccount(null);
        assertThat(localtransfer.getBankAccount()).isNull();
    }
}

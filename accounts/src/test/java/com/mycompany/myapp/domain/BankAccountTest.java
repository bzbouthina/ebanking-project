package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BankAccountTestSamples.*;
import static com.mycompany.myapp.domain.InternatonalTransferTestSamples.*;
import static com.mycompany.myapp.domain.LocaltransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BankAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccount.class);
        BankAccount bankAccount1 = getBankAccountSample1();
        BankAccount bankAccount2 = new BankAccount();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);

        bankAccount2.setId(bankAccount1.getId());
        assertThat(bankAccount1).isEqualTo(bankAccount2);

        bankAccount2 = getBankAccountSample2();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
    }

    @Test
    void localtransferTest() {
        BankAccount bankAccount = getBankAccountRandomSampleGenerator();
        Localtransfer localtransferBack = getLocaltransferRandomSampleGenerator();

        bankAccount.addLocaltransfer(localtransferBack);
        assertThat(bankAccount.getLocaltransfers()).containsOnly(localtransferBack);
        assertThat(localtransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.removeLocaltransfer(localtransferBack);
        assertThat(bankAccount.getLocaltransfers()).doesNotContain(localtransferBack);
        assertThat(localtransferBack.getBankAccount()).isNull();

        bankAccount.localtransfers(new HashSet<>(Set.of(localtransferBack)));
        assertThat(bankAccount.getLocaltransfers()).containsOnly(localtransferBack);
        assertThat(localtransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.setLocaltransfers(new HashSet<>());
        assertThat(bankAccount.getLocaltransfers()).doesNotContain(localtransferBack);
        assertThat(localtransferBack.getBankAccount()).isNull();
    }

    @Test
    void internatonalTransferTest() {
        BankAccount bankAccount = getBankAccountRandomSampleGenerator();
        InternatonalTransfer internatonalTransferBack = getInternatonalTransferRandomSampleGenerator();

        bankAccount.addInternatonalTransfer(internatonalTransferBack);
        assertThat(bankAccount.getInternatonalTransfers()).containsOnly(internatonalTransferBack);
        assertThat(internatonalTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.removeInternatonalTransfer(internatonalTransferBack);
        assertThat(bankAccount.getInternatonalTransfers()).doesNotContain(internatonalTransferBack);
        assertThat(internatonalTransferBack.getBankAccount()).isNull();

        bankAccount.internatonalTransfers(new HashSet<>(Set.of(internatonalTransferBack)));
        assertThat(bankAccount.getInternatonalTransfers()).containsOnly(internatonalTransferBack);
        assertThat(internatonalTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.setInternatonalTransfers(new HashSet<>());
        assertThat(bankAccount.getInternatonalTransfers()).doesNotContain(internatonalTransferBack);
        assertThat(internatonalTransferBack.getBankAccount()).isNull();
    }
}

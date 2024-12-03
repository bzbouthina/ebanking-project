package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.CustomerTestSamples.*;
import static com.myproject.myapp.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void customerTest() {
        Document document = getDocumentRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        document.setCustomer(customerBack);
        assertThat(document.getCustomer()).isEqualTo(customerBack);

        document.customer(null);
        assertThat(document.getCustomer()).isNull();
    }
}

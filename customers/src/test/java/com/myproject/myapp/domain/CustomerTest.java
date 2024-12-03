package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.CustomerTestSamples.*;
import static com.myproject.myapp.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void documentTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        customer.addDocument(documentBack);
        assertThat(customer.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getCustomer()).isEqualTo(customer);

        customer.removeDocument(documentBack);
        assertThat(customer.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getCustomer()).isNull();

        customer.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(customer.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getCustomer()).isEqualTo(customer);

        customer.setDocuments(new HashSet<>());
        assertThat(customer.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getCustomer()).isNull();
    }
}

package customer;

import client.CustomerClient;
import dataFactory.CustomerDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Customer;
import model.ErrorResponse;
import org.apache.http.HttpStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerDeleteTest {
    private final CustomerClient customerClient = new CustomerClient();

    @Test
    @DisplayName("Customer deletion by valid id test with success")
    @Description("This test attempts to delete an already saved customer.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteCustomerByIdWithSuccess() {
        Customer customer = CustomerDataFactory.getCustomerFromAPI();

        customerClient.deleteById(customer.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Customer deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved customer using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteCustomerByIdWithInvalidIdWithoutSuccess() {
        String id = CustomerDataFactory.getInvalidId();

        ErrorResponse errorResponse = customerClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Customer deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved customer using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteCustomerByIdWithEmptyIdWithoutSuccess() {
        String id = CustomerDataFactory.getEmptyId();

        ErrorResponse errorResponse = customerClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }
}

package customer;

import client.CustomerClient;
import dataFactory.CustomerDataFactory;
import model.Customer;
import model.ErrorResponse;
import org.apache.http.HttpStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerDeleteTest {
    private final CustomerClient customerClient = new CustomerClient();

    @Test
    public void mustDeleteCustomerByIdWithSuccess() {
        Customer customer = CustomerDataFactory.getCustomerFromAPI();

        customerClient.deleteById(customer.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void mustDeleteCustomerByIdWithInvalidIdWithoutSuccess() {
        String id = CustomerDataFactory.returnInvalidId();

        ErrorResponse errorResponse = customerClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    public void mustDeleteCustomerByIdWithEmptyIdWithoutSuccess() {
        String id = CustomerDataFactory.returnEmptyId();

        ErrorResponse errorResponse = customerClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }
}

package customer;

import client.CustomerClient;
import dataFactory.CustomerDataFactory;
import model.Customer;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomerGetTest {

    private final CustomerClient customerClient = new CustomerClient();

    @Test
    public void mustGetAllCustomersWithSuccess() {
        Customer[] getAllResponse = customerClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Customer[].class);

        assertAll("Grouped Customer Assertions",
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNull(customer.getAppointments())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getId())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getName())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getPhoneNumber())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getBirthday())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(customer -> assertNotNull(customer.getUpdatedAt()))
        );

    }

    @Test
    public void mustGetCustomerByIdWithSuccess() {
        Customer customer = CustomerDataFactory.getCustomerFromAPI();

        Customer getByIdResponse = customerClient.getById(customer.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Customer.class);

        assertAll("Grouped Customer Assertions",
                () -> assertEquals(customer.getId(), getByIdResponse.getId()),
                () -> assertEquals(customer.getName(), getByIdResponse.getName()),
                () -> assertEquals(customer.getPhoneNumber(), getByIdResponse.getPhoneNumber()),
                () -> assertEquals(customer.getBirthday(), getByIdResponse.getBirthday()),
                () -> assertEquals(customer.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(customer.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    public void mustGetCustomerByIdWithInvalidIdWithoutSuccess() {
        String id = CustomerDataFactory.getInvalidId();

        ErrorResponse errorResponse = customerClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }
}

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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerGetTest {
    private final CustomerClient customerClient = new CustomerClient();

    @Test
    @DisplayName("Get all Customers with success")
    @Description("This test attempts to find all customers saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllCustomersWithSuccess() {
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
    @DisplayName("Get a Customer by valid ID with success")
    @Description("This test attempts to find an already saved customer using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetCustomerByIdWithSuccess() {
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
    @DisplayName("Get a Customer by invalid ID without success")
    @Description("This test attempts to find an already saved customer using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetCustomerByIdWithInvalidIdWithoutSuccess() {
        String id = CustomerDataFactory.getInvalidId();

        ErrorResponse errorResponse = customerClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }
}

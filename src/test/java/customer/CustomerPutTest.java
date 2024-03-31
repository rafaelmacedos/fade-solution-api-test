package customer;

import client.CustomerClient;
import dataFactory.CustomerDataFactory;
import model.Customer;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerPutTest {
    private final CustomerClient customerClient = new CustomerClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    public void mustUpdateCustomerWithSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithUpdatedData();

        Customer putResponse = customerClient.put(customer, customer.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Customer.class);


        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped Customer Assertions",
                () -> assertEquals(customer.getId(), putResponse.getId()),
                () -> assertEquals(customer.getName(), putResponse.getName()),
                () -> assertEquals(customer.getPhoneNumber(), putResponse.getPhoneNumber()),
                () -> assertEquals(customer.getBirthday(), putResponse.getBirthday()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    public void mustUpdateCustomerWithInvalidIdWithoutSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithUpdatedData();
        customer.setId(null);
        String id = CustomerDataFactory.returnInvalidId();

        ErrorResponse errorResponse = customerClient.put(customer, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Customer not Founded. Id: " + id, errorResponse.getMessage());
    }
}

package customer;

import client.CustomerClient;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Customer;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataFactory.CustomerDataFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerPostTest {
    private final CustomerClient customerClient = new CustomerClient();

    @Test
    @DisplayName("Save a new valid Customer test with success")
    @Description("This test attempts to save a new valid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithSuccess() {
        Customer customer = CustomerDataFactory.validCustomer();

        Customer postResponse = customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Customer.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();


        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped Customer Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(customer.getName(), postResponse.getName()),
                () -> assertEquals(customer.getPhoneNumber(), postResponse.getPhoneNumber()),
                () -> assertEquals(customer.getBirthday(), postResponse.getBirthday()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid Customer test without success")
    @Description("This test attempts to save a new invalid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithEmptyNameWithoutSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithEmptyName();

        customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("name"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Customer test without success")
    @Description("This test attempts to save a new invalid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithEmptyPhoneNumberWithoutSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithEmptyPhoneNumber();

        customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(2))
                    .body("errors.field", containsInAnyOrder("phoneNumber", "phoneNumber"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "Número de Telefone deve ter tamanho 11"));

    }

    @Test
    @DisplayName("Save an invalid Customer test without success")
    @Description("This test attempts to save a new invalid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithBirthdayWithoutSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithEmptyBirthday();

        customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("birthday"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Customer test without success")
    @Description("This test attempts to save a new invalid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithInvalidPhoneNumberWithoutSuccess() {
        Customer customer = CustomerDataFactory.getCustomerWithInvalidPhoneNumber();

        customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("phoneNumber"))
                    .body("errors[0].error", equalTo("Número de Telefone deve ter tamanho 11"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Customer test without success")
    @Description("This test attempts to save a new invalid customer in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveCustomerWithEmptyDataWithoutSuccess() {
        Customer customer = CustomerDataFactory.getEmptyCustomer();

        customerClient.post(customer)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(3))
                    .body("errors.field", containsInAnyOrder("phoneNumber", "name", "birthday"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "must not be blank", "must not be null"));
        ;

    }

}

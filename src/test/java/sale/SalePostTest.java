package sale;

import client.SaleClient;
import dataFactory.SaleDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Sale;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalePostTest {
    private final SaleClient saleClient = new SaleClient();

    @Test
    @DisplayName("Save a new valid Sale test with success")
    @Description("This test attempts to save a new valid sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithSuccess() {
        Sale sale = SaleDataFactory.validSale();

        Sale postResponse = saleClient.post(sale)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Sale.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped Sale Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(sale.getItemsList(), postResponse.getItemsList()),
                () -> assertEquals(sale.getCustomerId(), postResponse.getCustomerId()),
                () -> assertEquals(sale.getBarberId(), postResponse.getBarberId()),
                () -> assertEquals(sale.getObservation(), postResponse.getObservation()),
                () -> assertEquals(sale.getPaymentMethod(), postResponse.getPaymentMethod()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid Sale test without success")
    @Description("This test attempts to save a new invalid Sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithEmptyBarberIdWithoutSuccess() {
        Sale Sale = SaleDataFactory.getSaleWithEmptyBarberId();

        saleClient.post(Sale)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("barberId"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Sale test without success")
    @Description("This test attempts to save a new invalid sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithEmptyCustomerIdWithoutSuccess() {
        Sale sale = SaleDataFactory.getSaleWithEmptyCustomerId();

        saleClient.post(sale)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(1))
                    .body("errors.field[0]", containsString("customerId"))
                    .body("errors.error[0]", containsString("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Sale test without success")
    @Description("This test attempts to save a new invalid sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithEmptyItemsList() {
        Sale sale = SaleDataFactory.getSaleWithEmptyItemsList();

        saleClient.post(sale)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("itemsList"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Sale test without success")
    @Description("This test attempts to save a new invalid sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithEmptyPaymentMethod() {
        Sale sale = SaleDataFactory.getSaleWithEmptyPaymentMethod();

        saleClient.post(sale)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("paymentMethod"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Sale test without success")
    @Description("This test attempts to save a new invalid sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveSaleWithEmptyDataWithoutSuccess() {
        Sale sale = SaleDataFactory.getEmptySale();

        saleClient.post(sale)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(6))
                    .body("errors.field", containsInAnyOrder("barberId", "barberId", "customerId", "customerId", "paymentMethod", "itemsList"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "must not be blank", "must not be null", "must not be null", "must not be null", "must not be null"));
        ;

    }

}

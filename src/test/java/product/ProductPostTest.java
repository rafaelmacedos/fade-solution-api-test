package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductPostTest {
    private final ProductClient productClient = new ProductClient();

    @Test
    @DisplayName("Save a new valid Product test with success")
    @Description("This test attempts to save a new valid product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveProductWithSuccess() {
        Product product = ProductDataFactory.validProduct();

        Product postResponse = productClient.post(product)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Product.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();


        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped Product Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(product.getName(), postResponse.getName()),
                () -> assertEquals(product.getPrice(), postResponse.getPrice()),
                () -> assertEquals(product.getDescription(), postResponse.getDescription()),
                () -> assertEquals(product.getQuantityInStock(), postResponse.getQuantityInStock()),
                () -> assertEquals(product.getCommissionPercentage(), postResponse.getCommissionPercentage()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid Product test without success")
    @Description("This test attempts to save a new invalid product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveProductWithEmptyNameWithoutSuccess() {
        Product product = ProductDataFactory.getProductWithEmptyName();

        productClient.post(product)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("name"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Product test without success")
    @Description("This test attempts to save a new invalid product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveProductWithEmptyPriceWithoutSuccess() {
        Product Product = ProductDataFactory.getProductWithEmptyPrice();

        productClient.post(Product)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(1))
                    .body("errors.field[0]", containsString("price"))
                    .body("errors.error[0]", containsString("must not be null"));

    }

    @Test
    @DisplayName("Save an invalid Product test without success")
    @Description("This test attempts to save a new invalid product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveProductWithNegativeValue() {
        Product product = ProductDataFactory.getProductWithNegativePrice();

        productClient.post(product)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("price"))
                    .body("errors[0].error", equalTo("must be greater than or equal to 0"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Product test without success")
    @Description("This test attempts to save a new invalid product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveProductWithEmptyDataWithoutSuccess() {
        Product product = ProductDataFactory.getEmptyProduct();

        productClient.post(product)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(7))
                    .body("errors.field", containsInAnyOrder("name", "brand", "brand", "price", "name", "quantityInStock", "commissionPercentage"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "must not be null", "must not be blank", "must not be null", "must not be null", "must not be null", "must not be null"));
        ;

    }

}

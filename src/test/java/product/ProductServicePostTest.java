package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import model.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServicePostTest {
    private final ProductClient productClient = new ProductClient();

    @Test
    public void mustSaveProductWithSuccess() {
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
    public void mustSaveProductWithEmptyNameWithoutSuccess() {
        Product product = ProductDataFactory.getProductWithEmptyName();

        productClient.post(product)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors[0].field", equalTo("name"))
                .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    public void mustSaveProductWithEmptyPriceWithoutSuccess() {
        Product Product = ProductDataFactory.getProductWithEmptyPrice();

        productClient.post(Product)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors", hasSize(1))
                .body("errors.field[0]", containsString("price"))
                .body("errors.error[0]", containsString("must not be null"));

    }

    @Test
    public void mustSaveProductWithNegativeValue() {
        Product product = ProductDataFactory.getProductWithNegativePrice();

        productClient.post(product)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors[0].field", equalTo("price"))
                .body("errors[0].error", equalTo("must be greater than or equal to 0"));
        ;

    }

    @Test
    public void mustSaveProductWithEmptyDataWithoutSuccess() {
        Product product = ProductDataFactory.getEmptyProduct();

        productClient.post(product)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors", hasSize(7))
                .body("errors.field", containsInAnyOrder("name", "brand", "brand", "price", "name","quantityInStock", "commissionPercentage"))
                .body("errors.error", containsInAnyOrder("must not be blank", "must not be null", "must not be blank", "must not be null", "must not be null", "must not be null", "must not be null"));
        ;

    }

}

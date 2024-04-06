package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.ErrorResponse;
import model.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ProductServicePutTest {
    private final ProductClient productClient = new ProductClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    @DisplayName("Update a Product by valid ID using valid data with success")
    @Description("This test attempts to update an existent product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateProductWithSuccess() {
        Product product = ProductDataFactory.getProductWithUpdatedData();

        Product putResponse = productClient.put(product, product.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Product.class);


        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped Product Assertions",
                () -> assertEquals(product.getId(), putResponse.getId()),
                () -> assertEquals(product.getName(), putResponse.getName()),
                () -> assertEquals(product.getPrice(), putResponse.getPrice()),
                () -> assertEquals(product.getDescription(), putResponse.getDescription()),
                () -> assertEquals(product.getQuantityInStock(), putResponse.getQuantityInStock()),
                () -> assertEquals(product.getCommissionPercentage(), putResponse.getCommissionPercentage()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    @DisplayName("Update a Product by invalid ID using valid data without success")
    @Description("This test attempts to update an existent product in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateProductWithInvalidIdWithoutSuccess() {
        Product product = ProductDataFactory.getProductWithUpdatedData();
        product.setId(null);
        String id = ProductDataFactory.getInvalidId();

        ErrorResponse errorResponse = productClient.put(product, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }
}

package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import model.ErrorResponse;
import model.Product;
import org.apache.http.HttpStatus;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDeleteTest {
    private final ProductClient productClient = new ProductClient();

    @Test
    @DisplayName("Product deletion by valid id test with success")
    @Description("This test attempts to delete an already saved product.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteProductByIdWithSuccess() {
        Product product = ProductDataFactory.getProductFromAPI();

        productClient.deleteById(product.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Product deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved product using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteProductByIdWithInvalidIdWithoutSuccess() {
        String id = ProductDataFactory.getInvalidId();

        ErrorResponse errorResponse = productClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Product deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved product using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteProductByIdWithEmptyIdWithoutSuccess() {
        String id = ProductDataFactory.getEmptyId();

        ErrorResponse errorResponse = productClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }
}

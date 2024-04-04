package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import model.ErrorResponse;
import model.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDeleteTest {
    private final ProductClient productClient = new ProductClient();

    @Test
    public void mustDeleteProductByIdWithSuccess() {
        Product product = ProductDataFactory.getProductFromAPI();

        productClient.deleteById(product.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void mustDeleteProductByIdWithInvalidIdWithoutSuccess() {
        String id = ProductDataFactory.getInvalidId();

        ErrorResponse errorResponse = productClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    public void mustDeleteProductByIdWithEmptyIdWithoutSuccess() {
        String id = ProductDataFactory.getEmptyId();

        ErrorResponse errorResponse = productClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }
}

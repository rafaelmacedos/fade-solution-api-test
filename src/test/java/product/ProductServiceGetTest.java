package product;

import client.ProductClient;
import dataFactory.ProductDataFactory;
import model.ErrorResponse;
import model.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceGetTest {

    private final ProductClient productClient = new ProductClient();

    @Test
    public void mustGetAllProductsWithSuccess() {
        Product[] getAllResponse = productClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Product[].class);

        assertAll("Grouped Product Assertions",
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getId())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getName())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getPrice())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getQuantityInStock())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getCommissionPercentage())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(product -> assertNotNull(product.getUpdatedAt()))
        );

    }

    @Test
    public void mustGetProductByIdWithSuccess() {
        Product product = ProductDataFactory.getProductFromAPI();

        Product getByIdResponse = productClient.getById(product.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Product.class);

        assertAll("Grouped Product Assertions",
                () -> assertEquals(product.getId(), getByIdResponse.getId()),
                () -> assertEquals(product.getName(), getByIdResponse.getName()),
                () -> assertEquals(product.getPrice(), getByIdResponse.getPrice()),
                () -> assertEquals(product.getDescription(), getByIdResponse.getDescription()),
                () -> assertEquals(product.getQuantityInStock(), getByIdResponse.getQuantityInStock()),
                () -> assertEquals(product.getCommissionPercentage(), getByIdResponse.getCommissionPercentage()),
                () -> assertEquals(product.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(product.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    public void mustGetProductByIdWithInvalidIdWithoutSuccess() {
        String id = ProductDataFactory.getInvalidId();

        ErrorResponse errorResponse = productClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Product not Founded. Id: " + id, errorResponse.getMessage());
    }
}

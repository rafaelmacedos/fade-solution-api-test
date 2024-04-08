package sale;

import client.SaleClient;
import dataFactory.SaleDataFactory;
import model.ErrorResponse;
import model.Sale;
import org.apache.http.HttpStatus;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleDeleteTest {
    private final SaleClient saleClient = new SaleClient();

    @Test
    @DisplayName("Sale deletion by valid id test with success")
    @Description("This test attempts to delete an already saved sale.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteSaleByIdWithSuccess() {
        Sale Sale = SaleDataFactory.getSaleFromAPI();

        saleClient.deleteById(Sale.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Sale deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved sale using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteSaleByIdWithInvalidIdWithoutSuccess() {
        String id = SaleDataFactory.getInvalidId();

        ErrorResponse errorResponse = saleClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Sale not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Sale deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved sale using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteSaleByIdWithEmptyIdWithoutSuccess() {
        String id = SaleDataFactory.getEmptyId();

        ErrorResponse errorResponse = saleClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Sale not Founded. Id: " + id, errorResponse.getMessage());
    }
}

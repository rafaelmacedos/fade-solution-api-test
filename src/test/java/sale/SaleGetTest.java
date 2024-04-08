package sale;

import client.SaleClient;
import dataFactory.SaleDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.ErrorResponse;
import model.Sale;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SaleGetTest {
    private final SaleClient saleClient = new SaleClient();

    @Test
    @DisplayName("Get all Sales with success")
    @Description("This test attempts to find all sales saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllSalesWithSuccess() {
        Sale[] getAllResponse = saleClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Sale[].class);

        assertAll("Grouped Sale Assertions",
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getId())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getBarberId())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getCustomerId())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getItemsList())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getPaymentMethod())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getTotalValue())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getTotalCommissionValue())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(sale -> assertNotNull(sale.getUpdatedAt()))
        );

    }

    @Test
    @DisplayName("Get a Sale by valid ID with success")
    @Description("This test attempts to find an already saved sale using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetSaleByIdWithSuccess() {
        Sale sale = SaleDataFactory.getSaleFromAPI();

        Sale getByIdResponse = saleClient.getById(sale.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Sale.class);

        assertAll("Grouped sale Assertions",
                () -> assertEquals(sale.getId(), getByIdResponse.getId()),
                () -> assertEquals(sale.getBarberId(), getByIdResponse.getBarberId()),
                () -> assertEquals(sale.getCustomerId(), getByIdResponse.getCustomerId()),
                () -> assertEquals(sale.getPaymentMethod(), getByIdResponse.getPaymentMethod()),
                () -> assertEquals(sale.getObservation(), getByIdResponse.getObservation()),
                () -> assertNotNull(getByIdResponse.getTotalValue()),
                () -> assertNotNull(getByIdResponse.getTotalCommissionValue()),
                () -> assertEquals(sale.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(sale.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Get a Sale by invalid ID without success")
    @Description("This test attempts to find an already saved sale using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetSaleByIdWithInvalidIdWithoutSuccess() {
        String id = SaleDataFactory.getInvalidId();

        ErrorResponse errorResponse = saleClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Sale not Founded. Id: " + id, errorResponse.getMessage());
    }
}

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class SalePutTest {
    private final SaleClient saleClient = new SaleClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    @DisplayName("Update a Sale by valid ID using valid data with success")
    @Description("This test attempts to update an existent sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateSaleWithSuccess() {
        Sale sale = SaleDataFactory.getSaleWithUpdatedData();

        Sale putResponse = saleClient.put(sale, sale.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Sale.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped sale Assertions",
                () -> assertEquals(sale.getId(), putResponse.getId()),
                () -> assertEquals(sale.getBarberId(), putResponse.getBarberId()),
                () -> assertEquals(sale.getCustomerId(), putResponse.getCustomerId()),
                () -> assertEquals(sale.getObservation(), putResponse.getObservation()),
                () -> assertEquals(sale.getPaymentMethod(), putResponse.getPaymentMethod()),
                () -> assertNotNull(putResponse.getTotalCommissionValue()),
                () -> assertNotNull(putResponse.getTotalValue()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    @DisplayName("Update a Sale by invalid ID using valid data without success")
    @Description("This test attempts to update an existent sale in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateSaleWithInvalidIdWithoutSuccess() {
        Sale sale = SaleDataFactory.getSaleWithUpdatedData();
        sale.setId(null);
        String id = SaleDataFactory.getInvalidId();

        ErrorResponse errorResponse = saleClient.put(sale, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                    .as(ErrorResponse.class);

        assertEquals("Entity Sale not Founded. Id: " + id, errorResponse.getMessage());
    }
}

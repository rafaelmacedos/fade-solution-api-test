package offeredService;

import client.OfferedServiceClient;
import dataFactory.OfferedServiceDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.OfferedService;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferedServiceDeleteTest {
    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();

    @Test
    @DisplayName("OfferedService deletion by valid id test with success")
    @Description("This test attempts to delete an already saved offeredService.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteOfferedServiceByIdWithSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.getOfferedServiceFromAPI();

        offeredServiceClient.deleteById(offeredService.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("OfferedService deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved offeredService using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteOfferedServiceByIdWithInvalidIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getInvalidId();

        ErrorResponse errorResponse = offeredServiceClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("OfferedService deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved offeredService using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteOfferedServiceByIdWithEmptyIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getEmptyId();

        ErrorResponse errorResponse = offeredServiceClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }
}

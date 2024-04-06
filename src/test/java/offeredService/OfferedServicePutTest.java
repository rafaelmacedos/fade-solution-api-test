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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class OfferedServicePutTest {
    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    @DisplayName("Update a OfferedService by valid ID using valid data with success")
    @Description("This test attempts to update an existent offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateOfferedServiceWithSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.getOfferedServiceWithUpdatedData();

        OfferedService putResponse = offeredServiceClient.put(offeredService, offeredService.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(OfferedService.class);


        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped offeredService Assertions",
                () -> assertEquals(offeredService.getId(), putResponse.getId()),
                () -> assertEquals(offeredService.getName(), putResponse.getName()),
                () -> assertEquals(offeredService.getValue(), putResponse.getValue()),
                () -> assertEquals(offeredService.getDescription(), putResponse.getDescription()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    @DisplayName("Update a OfferedService by invalid ID using valid data without success")
    @Description("This test attempts to update an existent offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateOfferedServiceWithInvalidIdWithoutSuccess() {
        OfferedService OfferedService = OfferedServiceDataFactory.getOfferedServiceWithUpdatedData();
        OfferedService.setId(null);
        String id = OfferedServiceDataFactory.getInvalidId();

        ErrorResponse errorResponse = offeredServiceClient.put(OfferedService, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }
}

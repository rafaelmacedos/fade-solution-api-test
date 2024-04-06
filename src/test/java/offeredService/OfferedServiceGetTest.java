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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OfferedServiceGetTest {
    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();

    @Test
    @DisplayName("Get all offeredServices with success")
    @Description("This test attempts to find all offeredService saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllOfferedServicesWithSuccess() {
        OfferedService[] getAllResponse = offeredServiceClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(OfferedService[].class);

        assertAll("Grouped OfferedService Assertions",
                () -> Arrays.stream(getAllResponse).forEach(OfferedService -> assertNotNull(OfferedService.getId())),
                () -> Arrays.stream(getAllResponse).forEach(OfferedService -> assertNotNull(OfferedService.getName())),
                () -> Arrays.stream(getAllResponse).forEach(OfferedService -> assertNotNull(OfferedService.getValue())),
                () -> Arrays.stream(getAllResponse).forEach(OfferedService -> assertNotNull(OfferedService.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(OfferedService -> assertNotNull(OfferedService.getUpdatedAt()))
        );

    }

    @Test
    @DisplayName("Get a OfferedService by valid ID with success")
    @Description("This test attempts to find an already saved offeredService using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetOfferedServiceByIdWithSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.getOfferedServiceFromAPI();

        OfferedService getByIdResponse = offeredServiceClient.getById(offeredService.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(OfferedService.class);

        assertAll("Grouped offeredService Assertions",
                () -> assertEquals(offeredService.getId(), getByIdResponse.getId()),
                () -> assertEquals(offeredService.getName(), getByIdResponse.getName()),
                () -> assertEquals(offeredService.getValue(), getByIdResponse.getValue()),
                () -> assertEquals(offeredService.getDescription(), getByIdResponse.getDescription()),
                () -> assertEquals(offeredService.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(offeredService.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Get a OfferedService by invalid ID without success")
    @Description("This test attempts to find an already saved offeredService using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetOfferedServiceByIdWithInvalidIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getInvalidId();

        ErrorResponse errorResponse = offeredServiceClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }
}

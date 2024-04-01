package offeredService;

import client.OfferedServiceClient;
import dataFactory.OfferedServiceDataFactory;
import model.OfferedService;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class OfferedServiceGetTest {

    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();

    @Test
    public void mustGetAllOfferedServicesWithSuccess() {
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
    public void mustGetOfferedServiceByIdWithSuccess() {
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
    public void mustGetOfferedServiceByIdWithInvalidIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getInvalidId();

        ErrorResponse errorResponse = offeredServiceClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }
}

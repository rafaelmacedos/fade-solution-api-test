package offeredService;

import client.OfferedServiceClient;
import dataFactory.OfferedServiceDataFactory;
import model.OfferedService;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferedServiceDeleteTest {
    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();

    @Test
    public void mustDeleteOfferedServiceByIdWithSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.getOfferedServiceFromAPI();

        offeredServiceClient.deleteById(offeredService.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void mustDeleteOfferedServiceByIdWithInvalidIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getInvalidId();

        ErrorResponse errorResponse = offeredServiceClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    public void mustDeleteOfferedServiceByIdWithEmptyIdWithoutSuccess() {
        String id = OfferedServiceDataFactory.getEmptyId();

        ErrorResponse errorResponse = offeredServiceClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Service not Founded. Id: " + id, errorResponse.getMessage());
    }
}

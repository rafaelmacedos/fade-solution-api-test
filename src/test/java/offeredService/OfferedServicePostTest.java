package offeredService;

import client.OfferedServiceClient;
import dataFactory.OfferedServiceDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.OfferedService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class OfferedServicePostTest {
    private final OfferedServiceClient offeredServiceClient = new OfferedServiceClient();

    @Test
    @DisplayName("Save a new valid OfferedService test with success")
    @Description("This test attempts to save a new valid offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveOfferedServiceWithSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.validOfferedService();

        OfferedService postResponse = offeredServiceClient.post(offeredService)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(OfferedService.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();


        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped offeredService Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(offeredService.getName(), postResponse.getName()),
                () -> assertEquals(offeredService.getValue(), postResponse.getValue()),
                () -> assertEquals(offeredService.getDescription(), postResponse.getDescription()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid OfferedService test without success")
    @Description("This test attempts to save a new invalid offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveOfferedServiceWithEmptyNameWithoutSuccess() {
        OfferedService offeredService = OfferedServiceDataFactory.getOfferedServiceWithEmptyName();

        offeredServiceClient.post(offeredService)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("name"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid OfferedService test without success")
    @Description("This test attempts to save a new invalid offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveOfferedServiceWithEmptyValueWithoutSuccess() {
        OfferedService OfferedService = OfferedServiceDataFactory.getOfferedServiceWithEmptyValue();

        offeredServiceClient.post(OfferedService)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(1))
                    .body("errors.field[0]", containsString("value"))
                    .body("errors.error[0]", containsString("must not be null"));

    }

    @Test
    @DisplayName("Save an invalid OfferedService test without success")
    @Description("This test attempts to save a new invalid offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveOfferedServiceWithNegativeValue() {
        OfferedService OfferedService = OfferedServiceDataFactory.getOfferedServiceWithNegativeValue();

        offeredServiceClient.post(OfferedService)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("value"))
                    .body("errors[0].error", equalTo("must be greater than or equal to 0"));
        ;

    }

    @Test
    @DisplayName("Save an invalid OfferedService test without success")
    @Description("This test attempts to save a new invalid offeredService in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveOfferedServiceWithEmptyDataWithoutSuccess() {
        OfferedService OfferedService = OfferedServiceDataFactory.getEmptyOfferedService();

        offeredServiceClient.post(OfferedService)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(3))
                    .body("errors.field", containsInAnyOrder("name", "value", "name"))
                    .body("errors.error", containsInAnyOrder("must not be null", "must not be blank", "must not be null"));
        ;

    }

}

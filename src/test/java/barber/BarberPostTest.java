package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Barber;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BarberPostTest {
    private final BarberClient BarberClient = new BarberClient();

    @Test
    @DisplayName("Save a new valid Barber test with success")
    @Description("This test attempts to save a new valid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithSuccess() {
        Barber barber = BarberDataFactory.validBarber();

        Barber postResponse = BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Barber.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();


        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped Barber Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(barber.getName(), postResponse.getName()),
                () -> assertEquals(barber.getPhoneNumber(), postResponse.getPhoneNumber()),
                () -> assertEquals(barber.getIsOwner(), postResponse.getIsOwner()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt),
                () -> assertTrue(postResponse.getIsActive())
        );
    }

    @Test
    @DisplayName("Save an invalid Barber test without success")
    @Description("This test attempts to save a new invalid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithEmptyNameWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyName();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("name"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Barber test without success")
    @Description("This test attempts to save a new invalid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithEmptyPhoneNumberWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyPhoneNumber();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(2))
                    .body("errors.field", containsInAnyOrder("phoneNumber", "phoneNumber"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "Número de Telefone deve ter tamanho 11"));

    }

    @Test
    @DisplayName("Save an invalid Barber test without success")
    @Description("This test attempts to save a new invalid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithEmptyIsOwnerWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyIsOwner();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("isOwner"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Barber test without success")
    @Description("This test attempts to save a new invalid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithInvalidPhoneNumberWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithInvalidPhoneNumber();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("phoneNumber"))
                    .body("errors[0].error", equalTo("Número de Telefone deve ter tamanho 11"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Barber test without success")
    @Description("This test attempts to save a new invalid barber in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveBarberWithEmptyDataWithoutSuccess() {
        Barber barber = BarberDataFactory.getEmptyBarber();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(6))
                    .body("errors.field", containsInAnyOrder("phoneNumber", "name", "isActive", "isOwner", "name", "phoneNumber"))
                    .body("errors.error", containsInAnyOrder("must not be null", "must not be null", "must not be null", "must not be null", "must not be blank", "must not be blank"));
        ;

    }

}

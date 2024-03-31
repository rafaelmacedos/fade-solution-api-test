package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import model.Barber;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class BarberPostTest {
    private final BarberClient BarberClient = new BarberClient();

    @Test
    public void mustSaveBarberWithSuccess() {
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
    public void mustSaveBarberWithEmptyNameWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyName();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("name"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    public void mustSaveBarberWithEmptyPhoneNumberWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyPhoneNumber();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(2))
                    .body("errors.field", containsInAnyOrder("phoneNumber", "phoneNumber"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "Número de Telefone deve ter tamanho 11"));

    }

    @Test
    public void mustSaveBarberWithEmptyIsOwnerWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithEmptyIsOwner();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("isOwner"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    public void mustSaveBarberWithInvalidPhoneNumberWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithInvalidPhoneNumber();

        BarberClient.post(barber)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("phoneNumber"))
                    .body("errors[0].error", equalTo("Número de Telefone deve ter tamanho 11"));
        ;

    }

    @Test
    public void mustSaveBarberWithEmptyDataWithoutSuccess() {
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

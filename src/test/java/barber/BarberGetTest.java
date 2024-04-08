package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Barber;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BarberGetTest {
    private final BarberClient barberClient = new BarberClient();

    @Test
    @DisplayName("Get all Barbers with success")
    @Description("This test attempts to find all barbers saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllBarbersWithSuccess() {
        Barber[] getAllResponse = barberClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Barber[].class);

        assertAll("Grouped Barber Assertions",
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNull(Barber.getAppointments())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getId())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getName())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getPhoneNumber())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getIsOwner())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getIsActive())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(Barber -> assertNotNull(Barber.getUpdatedAt()))
        );

    }

    @Test
    @DisplayName("Get a Barber by valid ID with success")
    @Description("This test attempts to find an already saved barber using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetBarberByIdWithSuccess() {
        Barber barber = BarberDataFactory.getBarberFromAPI();

        Barber getByIdResponse = barberClient.getById(barber.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Barber.class);

        assertAll("Grouped Barber Assertions",
                () -> assertEquals(barber.getId(), getByIdResponse.getId()),
                () -> assertEquals(barber.getName(), getByIdResponse.getName()),
                () -> assertEquals(barber.getPhoneNumber(), getByIdResponse.getPhoneNumber()),
                () -> assertEquals(barber.getIsOwner(), getByIdResponse.getIsOwner()),
                () -> assertEquals(barber.getIsActive(), getByIdResponse.getIsActive()),
                () -> assertEquals(barber.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(barber.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Get a Barber by invalid ID without success")
    @Description("This test attempts to find an already saved barber using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetBarberByIdWithInvalidIdWithoutSuccess() {
        String id = BarberDataFactory.getInvalidId();

        ErrorResponse errorResponse = barberClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }
}

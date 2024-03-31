package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import model.Barber;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BarberGetTest {

    private final BarberClient barberClient = new BarberClient();

    @Test
    public void mustGetAllBarbersWithSuccess() {
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
    public void mustGetBarberByIdWithSuccess() {
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
    public void mustGetBarberByIdWithInvalidIdWithoutSuccess() {
        String id = BarberDataFactory.getInvalidId();

        ErrorResponse errorResponse = barberClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }
}

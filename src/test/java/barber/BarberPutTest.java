package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import model.Barber;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class BarberPutTest {
    private final BarberClient barberClient = new BarberClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    public void mustUpdateBarberWithSuccess() {
        Barber barber = BarberDataFactory.getBarberWithUpdatedData();

        Barber putResponse = barberClient.put(barber, barber.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Barber.class);


        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped barber Assertions",
                () -> assertEquals(barber.getId(), putResponse.getId()),
                () -> assertEquals(barber.getName(), putResponse.getName()),
                () -> assertEquals(barber.getPhoneNumber(), putResponse.getPhoneNumber()),
                () -> assertEquals(barber.getIsOwner(), putResponse.getIsOwner()),
                () -> assertEquals(barber.getIsActive(), putResponse.getIsActive()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    public void mustUpdateBarberWithInvalidIdWithoutSuccess() {
        Barber barber = BarberDataFactory.getBarberWithUpdatedData();
        barber.setId(null);
        String id = BarberDataFactory.getInvalidId();

        ErrorResponse errorResponse = barberClient.put(barber, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }
}

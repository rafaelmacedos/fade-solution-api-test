package barber;

import client.BarberClient;
import dataFactory.BarberDataFactory;
import model.Barber;
import model.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberDeleteTest {
    private final BarberClient barberClient = new BarberClient();

    @Test
    public void mustDeleteBarberByIdWithSuccess() {
        Barber barber = BarberDataFactory.getBarberFromAPI();

        barberClient.deleteById(barber.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void mustDeleteBarberByIdWithInvalidIdWithoutSuccess() {
        String id = BarberDataFactory.getInvalidId();

        ErrorResponse errorResponse = barberClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    public void mustDeleteBarberByIdWithEmptyIdWithoutSuccess() {
        String id = BarberDataFactory.getEmptyId();

        ErrorResponse errorResponse = barberClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }
}

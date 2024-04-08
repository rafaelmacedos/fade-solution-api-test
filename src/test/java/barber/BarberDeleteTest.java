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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberDeleteTest {
    private final BarberClient barberClient = new BarberClient();

    @Test
    @DisplayName("Barber deletion by valid id test with success")
    @Description("This test attempts to delete an already saved barber.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteBarberByIdWithSuccess() {
        Barber barber = BarberDataFactory.getBarberFromAPI();

        barberClient.deleteById(barber.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Barber deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved barber using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteBarberByIdWithInvalidIdWithoutSuccess() {
        String id = BarberDataFactory.getInvalidId();

        ErrorResponse errorResponse = barberClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Barber deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved barber using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteBarberByIdWithEmptyIdWithoutSuccess() {
        String id = BarberDataFactory.getEmptyId();

        ErrorResponse errorResponse = barberClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Barber not Founded. Id: " + id, errorResponse.getMessage());
    }
}

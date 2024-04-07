package appointment;

import client.AppointmentClient;
import dataFactory.AppointmentDataFactory;
import model.ErrorResponse;
import model.Appointment;
import org.apache.http.HttpStatus;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentDeleteTest {
    private final AppointmentClient AppointmentClient = new AppointmentClient();

    @Test
    @DisplayName("Appointment deletion by valid id test with success")
    @Description("This test attempts to delete an already saved appointment.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteAppointmentByIdWithSuccess() {
        Appointment Appointment = AppointmentDataFactory.getAppointmentFromAPI();

        AppointmentClient.deleteById(Appointment.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Appointment deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved appointment using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteAppointmentByIdWithInvalidIdWithoutSuccess() {
        String id = AppointmentDataFactory.getInvalidId();

        ErrorResponse errorResponse = AppointmentClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Appointment not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Appointment deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved appointment using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteAppointmentByIdWithEmptyIdWithoutSuccess() {
        String id = AppointmentDataFactory.getEmptyId();

        ErrorResponse errorResponse = AppointmentClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Appointment not Founded. Id: " + id, errorResponse.getMessage());
    }
}

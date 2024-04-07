package appointment;

import client.AppointmentClient;
import dataFactory.AppointmentDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.ErrorResponse;
import model.Appointment;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentPutTest {
    private final AppointmentClient appointmentClient = new AppointmentClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    @DisplayName("Update a Appointment by valid ID using valid data with success")
    @Description("This test attempts to update an existent appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateAppointmentWithSuccess() {
        Appointment appointment = AppointmentDataFactory.getAppointmentWithUpdatedData();

        Appointment putResponse = appointmentClient.put(appointment, appointment.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Appointment.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped Appointment Assertions",
                () -> assertEquals(appointment.getId(), putResponse.getId()),
                () -> assertEquals(appointment.getBarberId(), putResponse.getBarberId()),
                () -> assertEquals(appointment.getCustomerId(), putResponse.getCustomerId()),
                () -> assertEquals(appointment.getObservation(), putResponse.getObservation()),
                () -> assertEquals(appointment.getPaymentMethod(), putResponse.getPaymentMethod()),
                () -> assertEquals(appointment.getStatus(), putResponse.getStatus()),
                () -> assertNotNull(putResponse.getTotalCommissionValue()),
                () -> assertNotNull(putResponse.getTotalServicesValue()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    @DisplayName("Update a Appointment by invalid ID using valid data without success")
    @Description("This test attempts to update an existent appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateAppointmentWithInvalidIdWithoutSuccess() {
        Appointment Appointment = AppointmentDataFactory.getAppointmentWithUpdatedData();
        Appointment.setId(null);
        String id = AppointmentDataFactory.getInvalidId();

        ErrorResponse errorResponse = appointmentClient.put(Appointment, id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Appointment not Founded. Id: " + id, errorResponse.getMessage());
    }
}

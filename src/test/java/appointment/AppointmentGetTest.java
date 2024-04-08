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


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentGetTest {
    private final AppointmentClient appointmentClient = new AppointmentClient();

    @Test
    @DisplayName("Get all Appointments with success")
    @Description("This test attempts to find all appointments saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllAppointmentsWithSuccess() {
        Appointment[] getAllResponse = appointmentClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Appointment[].class);

        assertAll("Grouped Appointment Assertions",
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getId())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getBarberId())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getCustomerId())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getItemsList())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getPaymentMethod())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getStatus())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getTotalServicesValue())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getTotalCommissionValue())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(appointment -> assertNotNull(appointment.getUpdatedAt()))
        );

    }

    @Test
    @DisplayName("Get a Appointment by valid ID with success")
    @Description("This test attempts to find an already saved appointment using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAppointmentByIdWithSuccess() {
        Appointment appointment = AppointmentDataFactory.getAppointmentFromAPI();

        Appointment getByIdResponse = appointmentClient.getById(appointment.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Appointment.class);

        assertAll("Grouped Appointment Assertions",
                () -> assertEquals(appointment.getId(), getByIdResponse.getId()),
                () -> assertEquals(appointment.getBarberId(), getByIdResponse.getBarberId()),
                () -> assertEquals(appointment.getCustomerId(), getByIdResponse.getCustomerId()),
                () -> assertEquals(appointment.getPaymentMethod(), getByIdResponse.getPaymentMethod()),
                () -> assertEquals(appointment.getStatus(), getByIdResponse.getStatus()),
                () -> assertEquals(appointment.getObservation(), getByIdResponse.getObservation()),
                () -> assertNotNull(getByIdResponse.getTotalServicesValue()),
                () -> assertNotNull(getByIdResponse.getTotalCommissionValue()),
                () -> assertEquals(appointment.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(appointment.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Get a Appointment by invalid ID without success")
    @Description("This test attempts to find an already saved appointment using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAppointmentByIdWithInvalidIdWithoutSuccess() {
        String id = AppointmentDataFactory.getInvalidId();

        ErrorResponse errorResponse = appointmentClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Appointment not Founded. Id: " + id, errorResponse.getMessage());
    }
}

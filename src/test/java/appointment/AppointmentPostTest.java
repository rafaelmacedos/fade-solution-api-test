package appointment;

import client.AppointmentClient;
import dataFactory.AppointmentDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Appointment;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppointmentPostTest {
    private final AppointmentClient appointmentClient = new AppointmentClient();

    @Test
    @DisplayName("Save a new valid Appointment test with success")
    @Description("This test attempts to save a new valid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithSuccess() {
        Appointment appointment = AppointmentDataFactory.validAppointment();

        Appointment postResponse = appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Appointment.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped appointment Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(appointment.getItemsList(), postResponse.getItemsList()),
                () -> assertEquals(appointment.getCustomerId(), postResponse.getCustomerId()),
                () -> assertEquals(appointment.getBarberId(), postResponse.getBarberId()),
                () -> assertEquals(appointment.getObservation(), postResponse.getObservation()),
                () -> assertEquals(appointment.getPaymentMethod(), postResponse.getPaymentMethod()),
                () -> assertEquals(appointment.getStatus(), postResponse.getStatus()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid Appointment test without success")
    @Description("This test attempts to save a new invalid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithEmptyBarberIdWithoutSuccess() {
        Appointment appointment = AppointmentDataFactory.getAppointmentWithEmptyBarberId();

        appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("barberId"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Appointment test without success")
    @Description("This test attempts to save a new invalid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithEmptyCustomerIdWithoutSuccess() {
        Appointment appointment = AppointmentDataFactory.getAppointmentWithEmptyCustomerId();

        appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(1))
                    .body("errors.field[0]", containsString("customerId"))
                    .body("errors.error[0]", containsString("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Appointment test without success")
    @Description("This test attempts to save a new invalid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithEmptyStatus() {
        Appointment appointment = AppointmentDataFactory.getAppointmentWithEmptyItemsList();

        appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("itemsList"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Appointment test without success")
    @Description("This test attempts to save a new invalid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithEmptyPaymentMethod() {
        Appointment appointment = AppointmentDataFactory.getAppointmentWithEmptyPaymentMethod();

        appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("paymentMethod"))
                    .body("errors[0].error", equalTo("must not be null"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Appointment test without success")
    @Description("This test attempts to save a new invalid appointment in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveAppointmentWithEmptyDataWithoutSuccess() {
        Appointment appointment = AppointmentDataFactory.getEmptyAppointment();

        appointmentClient.post(appointment)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(7))
                    .body("errors.field", containsInAnyOrder("barberId", "barberId", "customerId", "customerId", "status", "paymentMethod", "itemsList"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "must not be blank", "must not be null", "must not be null", "must not be null", "must not be null", "must not be null"));
        ;

    }

}

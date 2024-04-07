package dataFactory;

import client.AppointmentClient;
import com.github.javafaker.Faker;
import model.Appointment;
import model.Item;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public class AppointmentDataFactory {
    public static AppointmentClient appointmentClient = new AppointmentClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static Appointment validAppointment() {
        return newAppointment();
    }

    public static ArrayList<Item> getItemsList() {
        Random random = new Random();
        int numberOfItems = random.nextInt(1, 5);

        ArrayList<Item> itemsList = new ArrayList<>();
        IntStream.of(numberOfItems)
                .forEach((i) -> itemsList.add(
                        new Item(OfferedServiceDataFactory.getOfferedServiceFromAPI(), random.nextInt(1, 3)))
                );
        return itemsList;
    }

    public static Appointment newAppointment() {
        ArrayList<Item> itemsList = getItemsList();

        Random random = new Random();
        String[] paymentMethodList = {"CREDIT", "DEBIT", "CASH", "PIX", "COURTESY"};
        int randomIndex1 = random.nextInt(paymentMethodList.length);
        String paymentMethod = paymentMethodList[randomIndex1];

        String[] statusList = {"AWAITING_PAYMENT", "PAID", "COURTESY"};
        int randomIndex2 = random.nextInt(statusList.length);
        String status = statusList[randomIndex2];

        String barberId = BarberDataFactory.getBarberFromAPI().getId();
        String customerId = CustomerDataFactory.getCustomerFromAPI().getId();
        String observation = FAKER.lorem().sentence(10);

        return new Appointment(itemsList, barberId, customerId, observation, paymentMethod, status);
    }

    public static Appointment getAppointmentWithEmptyBarberId() {
        Appointment appointment = newAppointment();
        appointment.setBarberId("");
        return appointment;
    }

    public static Appointment getAppointmentWithEmptyCustomerId() {
        Appointment appointment = newAppointment();
        appointment.setCustomerId("");
        return appointment;
    }

    public static Appointment getAppointmentWithEmptyObservation() {
        Appointment appointment = newAppointment();
        appointment.setObservation("");
        return appointment;
    }

    public static Appointment getAppointmentWithEmptyItemsList() {
        Appointment appointment = newAppointment();
        appointment.setItemsList(null);
        return appointment;
    }

    public static Appointment getAppointmentWithEmptyPaymentMethod() {
        Appointment appointment = newAppointment();
        appointment.setPaymentMethod(null);
        return appointment;
    }

    public static Appointment getAppointmentWithEmptyStatus() {
        Appointment appointment = newAppointment();
        appointment.setStatus("");
        return appointment;
    }

    public static Appointment getEmptyAppointment() {
        return new Appointment();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Appointment getAppointmentFromAPI() {
        Appointment postResponse = appointmentClient.post(validAppointment())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(Appointment.class);

        Appointment getByIdResponse = appointmentClient.getById(postResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Appointment.class);

        return getByIdResponse;
    }

    public static Appointment getAppointmentWithUpdatedData() {
        Appointment appointment = getAppointmentFromAPI();
        appointment.setItemsList(getItemsList());

        Random random = new Random();
        String[] paymentMethodList = {"CREDIT", "DEBIT", "CASH", "PIX", "COURTESY"};
        int randomIndex1 = random.nextInt(paymentMethodList.length);
        appointment.setPaymentMethod(paymentMethodList[randomIndex1]);

        String[] statusList = {"AWAITING_PAYMENT", "PAID", "COURTESY"};
        int randomIndex2 = random.nextInt(statusList.length);
        appointment.setStatus(statusList[randomIndex2]);

        appointment.setBarberId(BarberDataFactory.getBarberFromAPI().getId());
        appointment.setCustomerId(CustomerDataFactory.getCustomerFromAPI().getId());
        appointment.setBarberName(null);
        appointment.setCustomerName(null);
        appointment.setObservation(FAKER.lorem().sentence(10));
        appointment.setCreatedAt(null);
        appointment.setUpdatedAt(null);
        appointment.setTotalCommissionValue(null);
        appointment.setTotalServicesValue(null);

        return appointment;
    }
}
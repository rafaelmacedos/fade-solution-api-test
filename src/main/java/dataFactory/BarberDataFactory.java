package dataFactory;

import client.BarberClient;
import com.github.javafaker.Faker;
import model.Barber;
import org.apache.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BarberDataFactory {
    public static BarberClient barberClient = new BarberClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static Barber validBarber() {
        return newBarber();
    }

    public static Barber newBarber() {
        String name = FAKER.name().fullName();
        String phoneNumber = FAKER.numerify("##9########");
        Boolean isOwner = FAKER.bool().bool();
        Boolean isActive = true;

        return new Barber(name, phoneNumber, isOwner, isActive);
    }

    public static Barber getBarberWithEmptyName() {
        Barber barber = newBarber();
        barber.setName("");
        return barber;
    }

    public static Barber getBarberWithEmptyPhoneNumber() {
        Barber barber = newBarber();
        barber.setPhoneNumber("");
        return barber;
    }

    public static Barber getBarberWithInvalidPhoneNumber() {
        Barber barber = newBarber();
        barber.setPhoneNumber(FAKER.numerify("#############"));
        return barber;
    }

    public static Barber getBarberWithEmptyIsOwner() {
        Barber barber = newBarber();
        barber.setIsOwner(null);
        return barber;
    }

    public static Barber getEmptyBarber() {
        return new Barber();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Barber getBarberFromAPI() {
        Barber postResponse = barberClient.post(validBarber())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(Barber.class);

        Barber getByIdResponse = barberClient.getById(postResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Barber.class);

        return getByIdResponse;
    }

    public static Barber getBarberWithUpdatedData() {
        Barber barber = getBarberFromAPI();
        barber.setName(FAKER.name().fullName());
        barber.setPhoneNumber(FAKER.numerify("##9########"));
        barber.setIsOwner(FAKER.bool().bool());
        barber.setIsActive(FAKER.bool().bool());
        barber.setCreatedAt(null);
        barber.setUpdatedAt(null);
        barber.setAppointments(null);
        return barber;
    }
}
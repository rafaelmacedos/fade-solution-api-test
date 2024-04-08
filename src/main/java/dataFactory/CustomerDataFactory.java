package dataFactory;

import client.CustomerClient;
import com.github.javafaker.Faker;
import model.Customer;
import org.apache.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomerDataFactory {
    public static CustomerClient customerClient = new CustomerClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Customer validCustomer() {
        return newCustomer();
    }

    public static Customer newCustomer() {
        String name = FAKER.name().fullName();
        String phoneNumber = FAKER.numerify("##9########");
        String birthday = dateFormat.format(FAKER.date().birthday());

        return new Customer(name, phoneNumber, birthday);
    }

    public static Customer getCustomerWithEmptyName() {
        Customer customer = newCustomer();
        customer.setName("");
        return customer;
    }

    public static Customer getCustomerWithEmptyPhoneNumber() {
        Customer customer = newCustomer();
        customer.setPhoneNumber("");
        return customer;
    }

    public static Customer getCustomerWithInvalidPhoneNumber() {
        Customer customer = newCustomer();
        customer.setPhoneNumber(FAKER.numerify("##9#########"));
        return customer;
    }

    public static Customer getCustomerWithEmptyBirthday() {
        Customer customer = newCustomer();
        customer.setBirthday("");
        return customer;
    }

    public static Customer getEmptyCustomer() {
        return new Customer();
    }
    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Customer getCustomerFromAPI() {
        Customer postResponse = customerClient.post(validCustomer())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(Customer.class);

        Customer getByIdResponse = customerClient.getById(postResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Customer.class);

        return getByIdResponse;
    }

    public static Customer getCustomerWithUpdatedData() {
        Customer customer = getCustomerFromAPI();
        customer.setName(FAKER.name().fullName());
        customer.setPhoneNumber(FAKER.numerify("##9########"));
        customer.setBirthday(dateFormat.format(FAKER.date().birthday()));
        customer.setCreatedAt(null);
        customer.setUpdatedAt(null);
        customer.setAppointments(null);
        return customer;
    }
}
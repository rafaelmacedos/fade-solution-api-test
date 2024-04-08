package dataFactory;

import client.OfferedServiceClient;
import com.github.javafaker.Faker;
import model.OfferedService;
import org.apache.http.HttpStatus;

import java.util.Locale;

public class OfferedServiceDataFactory {
    public static OfferedServiceClient offeredServiceClient = new OfferedServiceClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static OfferedService validOfferedService() {
        return newOfferedService();
    }

    public static OfferedService newOfferedService() {
        String name = FAKER.commerce().productName();
        String description = FAKER.lorem().sentence(10);
        Double value = FAKER.number().randomDouble(2, 10, 300);

        return new OfferedService(name, description, value);
    }

    public static OfferedService getOfferedServiceWithEmptyName() {
        OfferedService offeredService = newOfferedService();
        offeredService.setName("");
        return offeredService;
    }

    public static OfferedService getOfferedServiceWithEmptyDescription() {
        OfferedService offeredService = newOfferedService();
        offeredService.setDescription("");
        return offeredService;
    }

    public static OfferedService getOfferedServiceWithNegativeValue() {
        OfferedService offeredService = newOfferedService();
        offeredService.setValue(-50.0);
        return offeredService;
    }

    public static OfferedService getOfferedServiceWithEmptyValue() {
        OfferedService offeredService = newOfferedService();
        offeredService.setValue(null);
        return offeredService;
    }

    public static OfferedService getEmptyOfferedService() {
        return new OfferedService();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static OfferedService getOfferedServiceFromAPI() {
        OfferedService postResponse = offeredServiceClient.post(validOfferedService())
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(OfferedService.class);

        OfferedService getByIdResponse = offeredServiceClient.getById(postResponse.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(OfferedService.class);

        return getByIdResponse;
    }

    public static OfferedService getOfferedServiceWithUpdatedData() {
        OfferedService offeredService = getOfferedServiceFromAPI();
        offeredService.setName(FAKER.lorem().characters(5, 40, true));
        offeredService.setDescription(FAKER.lorem().sentence(10));
        offeredService.setValue(FAKER.number().randomDouble(2, 10, 300));
        offeredService.setCreatedAt(null);
        offeredService.setUpdatedAt(null);

        return offeredService;
    }
}
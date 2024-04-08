package dataFactory;

import client.SaleClient;
import com.github.javafaker.Faker;
import model.Sale;
import model.Item;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

public class SaleDataFactory {
    public static SaleClient saleClient = new SaleClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static Sale validSale() {
        return newSale();
    }

    public static ArrayList<Item> getItemsList() {
        Random random = new Random();
        int numberOfItems = random.nextInt(1, 5);

        ArrayList<Item> itemsList = new ArrayList<>();
        IntStream.of(numberOfItems)
                .forEach((i) -> itemsList.add(
                        new Item(ProductDataFactory.getProductFromAPI(), random.nextInt(1, 3)))
                );
        return itemsList;
    }

    public static Sale newSale() {
        ArrayList<Item> itemsList = getItemsList();

        Random random = new Random();
        String[] paymentMethodList = {"CREDIT", "DEBIT", "CASH", "PIX", "COURTESY"};
        int randomIndex1 = random.nextInt(paymentMethodList.length);
        String paymentMethod = paymentMethodList[randomIndex1];

        String barberId = BarberDataFactory.getBarberFromAPI().getId();
        String customerId = CustomerDataFactory.getCustomerFromAPI().getId();
        String observation = FAKER.lorem().sentence(10);

        return new Sale(itemsList, barberId, customerId, observation, paymentMethod);
    }

    public static Sale getSaleWithEmptyBarberId() {
        Sale Sale = newSale();
        Sale.setBarberId("");
        return Sale;
    }

    public static Sale getSaleWithEmptyCustomerId() {
        Sale Sale = newSale();
        Sale.setCustomerId("");
        return Sale;
    }

    public static Sale getSaleWithEmptyObservation() {
        Sale Sale = newSale();
        Sale.setObservation("");
        return Sale;
    }

    public static Sale getSaleWithEmptyItemsList() {
        Sale Sale = newSale();
        Sale.setItemsList(null);
        return Sale;
    }

    public static Sale getSaleWithEmptyPaymentMethod() {
        Sale Sale = newSale();
        Sale.setPaymentMethod(null);
        return Sale;
    }

    public static Sale getEmptySale() {
        return new Sale();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Sale getSaleFromAPI() {
        Sale postResponse = saleClient.post(validSale())
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Sale.class);

        Sale getByIdResponse = saleClient.getById(postResponse.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Sale.class);

        return getByIdResponse;
    }

    public static Sale getSaleWithUpdatedData() {
        Sale Sale = getSaleFromAPI();
        Sale.setItemsList(getItemsList());

        Random random = new Random();
        String[] paymentMethodList = {"CREDIT", "DEBIT", "CASH", "PIX", "COURTESY"};
        int randomIndex1 = random.nextInt(paymentMethodList.length);
        Sale.setPaymentMethod(paymentMethodList[randomIndex1]);

        Sale.setBarberId(BarberDataFactory.getBarberFromAPI().getId());
        Sale.setCustomerId(CustomerDataFactory.getCustomerFromAPI().getId());
        Sale.setBarberName(null);
        Sale.setCustomerName(null);
        Sale.setObservation(FAKER.lorem().sentence(10));
        Sale.setCreatedAt(null);
        Sale.setUpdatedAt(null);
        Sale.setTotalCommissionValue(null);
        Sale.setTotalValue(null);

        return Sale;
    }
}
package dataFactory;

import client.ProductClient;
import com.github.javafaker.Faker;
import model.Product;
import org.apache.http.HttpStatus;

import java.util.Locale;
import java.util.Random;

public class ProductDataFactory {
    public static ProductClient productClient = new ProductClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static Product validProduct() {
        return newProduct();
    }

    public static Product newProduct() {
        String name = FAKER.commerce().productName();

        String[] brandList = {"BABOON", "CABALLEROS", "KIRKLAND", "DON ALCIDES", "BLEND ORIGINAL", "VIKING", "ALFA LOOKS"};
        Random random = new Random();
        int randomIndex = random.nextInt(brandList.length);
        String brand = brandList[randomIndex];

        String description = FAKER.lorem().sentence(10);
        Double price = FAKER.number().randomDouble(2, 10, 700);
        Integer quantityInStock = FAKER.number().numberBetween(1, 100);
        Integer commissionPercentage = FAKER.number().numberBetween(10, 50);

        return new Product(name, brand, description, price, quantityInStock, commissionPercentage);
    }

    public static Product getProductWithEmptyName() {
        Product product = newProduct();
        product.setName("");
        return product;
    }

    public static Product getProductWithEmptyDescription() {
        Product product = newProduct();
        product.setDescription("");
        return product;
    }

    public static Product getProductWithNegativePrice() {
        Product product = newProduct();
        product.setPrice(-50.0);
        return product;
    }

    public static Product getProductWithEmptyPrice() {
        Product product = newProduct();
        product.setPrice(null);
        return product;
    }

    public static Product getEmptyProduct() {
        return new Product();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Product getProductFromAPI() {
        Product postResponse = productClient.post(validProduct())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(Product.class);

        Product getByIdResponse = productClient.getById(postResponse.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Product.class);

        return getByIdResponse;
    }

    public static Product getProductWithUpdatedData() {
        Product product = getProductFromAPI();
        product.setName(FAKER.commerce().productName());

        String[] brandList = {"BABOON", "CABALLEROS", "KIRKLAND", "DON ALCIDES", "BLEND ORIGINAL", "VIKING", "ALFA LOOKS", "FOX"};
        Random random = new Random();
        int randomIndex = random.nextInt(brandList.length);
        product.setBrand(brandList[randomIndex]);

        product.setDescription(FAKER.lorem().sentence(10));
        product.setPrice(FAKER.number().randomDouble(2, 10, 700));
        product.setQuantityInStock(FAKER.number().numberBetween(1, 100));
        product.setCommissionPercentage(FAKER.number().numberBetween(10, 500));
        product.setCreatedAt(null);
        product.setUpdatedAt(null);

        return product;
    }
}
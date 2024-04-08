package dataFactory;

import client.BarberClient;
import client.ExpenseClient;
import com.github.javafaker.Faker;
import model.Expense;
import org.apache.http.HttpStatus;

import java.util.Locale;
import java.util.Random;

public class ExpenseDataFactory {
    public static ExpenseClient expenseClient = new ExpenseClient();
    private static final Faker FAKER = new Faker(new Locale("PT-BR"));

    public static Expense validExpense() {
        return newExpense();
    }

    public static Expense newExpense() {
        String barberId = BarberDataFactory.getBarberFromAPI().getId();
        String description = FAKER.lorem().sentence(10);

        String[] expenseTypeList = {"PRODUCT_ACQUISITION", "HIRED_SERVICE", "RENT_BILL", "WATER_BILL", "ENERGY_BILL", "INTERNET_BILL", "FOOD", "DEDUCTION", "OTHER"};
        Random random = new Random();
        int randomIndex = random.nextInt(expenseTypeList.length);
        String expenseType = expenseTypeList[randomIndex];

        Double totalValue = FAKER.number().randomDouble(2, 10, 15000);

        return new Expense(barberId, description, expenseType, totalValue);
    }

    public static Expense getExpenseWithEmptyBarberId() {
        Expense expense = newExpense();
        expense.setBarberId("");
        return expense;
    }

    public static Expense getExpenseWithEmptyDescription() {
        Expense expense = newExpense();
        expense.setDescription("");
        return expense;
    }

    public static Expense getExpenseWithNegativeTotalValue() {
        Expense expense = newExpense();
        expense.setTotalValue(-50.0);
        return expense;
    }

    public static Expense getExpenseWithEmptyTotalValue() {
        Expense expense = newExpense();
        expense.setTotalValue(null);
        return expense;
    }

    public static Expense getEmptyExpense() {
        return new Expense();
    }

    public static String getInvalidId() {
        return "763224859";
    }

    public static String getEmptyId() {
        return " ";
    }

    public static Expense getExpenseFromAPI() {
        Expense postResponse = expenseClient.post(validExpense())
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Expense.class);

        Expense getByIdResponse = expenseClient.getById(postResponse.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Expense.class);

        return getByIdResponse;
    }

    public static Expense getExpenseWithUpdatedData() {
        Expense expense = getExpenseFromAPI();
        String barberId = BarberDataFactory.getBarberFromAPI().getId();
        expense.setBarberId(barberId);
        expense.setDescription(FAKER.lorem().sentence(10));

        String[] expenseTypeList = {"PRODUCT_ACQUISITION", "HIRED_SERVICE", "RENT_BILL", "WATER_BILL", "ENERGY_BILL", "INTERNET_BILL", "FOOD", "DEDUCTION", "OTHER"};
        Random random = new Random();
        int randomIndex = random.nextInt(expenseTypeList.length);
        expense.setExpenseType(expenseTypeList[randomIndex]);

        expense.setTotalValue(FAKER.number().randomDouble(2, 10, 15000));

        expense.setCreatedAt(null);
        expense.setUpdatedAt(null);
        expense.setBarberName(null);

        return expense;
    }
}
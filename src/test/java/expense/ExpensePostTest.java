package expense;

import client.ExpenseClient;
import dataFactory.ExpenseDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.Expense;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpensePostTest {
    private final ExpenseClient expenseClient = new ExpenseClient();

    @Test
    @DisplayName("Save a new valid Expense test with success")
    @Description("This test attempts to save a new valid expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveExpenseWithSuccess() {
        Expense expense = ExpenseDataFactory.validExpense();

        Expense postResponse = expenseClient.post(expense)
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                        .as(Expense.class);

        LocalDate createdAtToLocalDate = LocalDate.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDate todayToLocalDate = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(postResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);

        assertAll("Grouped expense Assertions",
                () -> assertNotNull(postResponse.getId()),
                () -> assertEquals(expense.getBarberId(), postResponse.getBarberId()),
                () -> assertEquals(expense.getDescription(), postResponse.getDescription()),
                () -> assertEquals(expense.getExpenseType(), postResponse.getExpenseType()),
                () -> assertEquals(expense.getTotalValue(), postResponse.getTotalValue()),
                () -> assertEquals(todayToLocalDate, createdAtToLocalDate),
                () -> assertEquals(createdAt, updatedAt)
        );
    }

    @Test
    @DisplayName("Save an invalid Expense test without success")
    @Description("This test attempts to save a new invalid expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveExpenseWithEmptyBarberIdWithoutSuccess() {
        Expense expense = ExpenseDataFactory.getExpenseWithEmptyBarberId();

        expenseClient.post(expense)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("barberId"))
                    .body("errors[0].error", equalTo("must not be blank"));

    }

    @Test
    @DisplayName("Save an invalid Expense test without success")
    @Description("This test attempts to save a new invalid expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveExpenseWithEmptyTotalValueWithoutSuccess() {
        Expense expense = ExpenseDataFactory.getExpenseWithEmptyTotalValue();

        expenseClient.post(expense)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(1))
                    .body("errors.field[0]", containsString("totalValue"))
                    .body("errors.error[0]", containsString("must not be null"));

    }

    @Test
    @DisplayName("Save an invalid Expense test without success")
    @Description("This test attempts to save a new invalid expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveExpenseWithNegativeTotalValue() {
        Expense expense = ExpenseDataFactory.getExpenseWithNegativeTotalValue();

        expenseClient.post(expense)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors[0].field", equalTo("totalValue"))
                    .body("errors[0].error", equalTo("must be greater than or equal to 0"));
        ;

    }

    @Test
    @DisplayName("Save an invalid Expense test without success")
    @Description("This test attempts to save a new invalid expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldSaveExpenseWithEmptyDataWithoutSuccess() {
        Expense expense = ExpenseDataFactory.getEmptyExpense();

        expenseClient.post(expense)
                .then()
                    .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                    .body("errors", hasSize(6))
                    .body("errors.field", containsInAnyOrder("totalValue", "description", "barberId", "barberId", "description", "expenseType"))
                    .body("errors.error", containsInAnyOrder("must not be blank", "must not be null", "must not be null", "must not be blank", "must not be null", "must not be null"));
        ;

    }

}

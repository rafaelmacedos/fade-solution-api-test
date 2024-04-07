package expense;

import client.ExpenseClient;
import dataFactory.ExpenseDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import model.ErrorResponse;
import model.Expense;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseGetTest {
    private final ExpenseClient expenseClient = new ExpenseClient();

    @Test
    @DisplayName("Get all Expenses with success")
    @Description("This test attempts to find all expenses saved in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetAllExpensesWithSuccess() {
        Expense[] getAllResponse = expenseClient.getAll()
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Expense[].class);

        assertAll("Grouped Expense Assertions",
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getId())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getBarberId())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getDescription())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getExpenseType())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getTotalValue())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getBarberName())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getCreatedAt())),
                () -> Arrays.stream(getAllResponse).forEach(expense -> assertNotNull(expense.getUpdatedAt()))
        );

    }

    @Test
    @DisplayName("Get a Expense by valid ID with success")
    @Description("This test attempts to find an already saved expense using an valid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetExpenseByIdWithSuccess() {
        Expense expense = ExpenseDataFactory.getExpenseFromAPI();

        Expense getByIdResponse = expenseClient.getById(expense.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                        .as(Expense.class);

        assertAll("Grouped expense Assertions",
                () -> assertEquals(expense.getId(), getByIdResponse.getId()),
                () -> assertEquals(expense.getBarberId(), getByIdResponse.getBarberId()),
                () -> assertEquals(expense.getDescription(), getByIdResponse.getDescription()),
                () -> assertEquals(expense.getExpenseType(), getByIdResponse.getExpenseType()),
                () -> assertEquals(expense.getTotalValue(), getByIdResponse.getTotalValue()),
                () -> assertNotNull(getByIdResponse.getBarberName()),
                () -> assertEquals(expense.getCreatedAt(), getByIdResponse.getUpdatedAt()),
                () -> assertEquals(expense.getUpdatedAt(), getByIdResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Get a Expense by invalid ID without success")
    @Description("This test attempts to find an already saved expense using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldGetExpenseByIdWithInvalidIdWithoutSuccess() {
        String id = ExpenseDataFactory.getInvalidId();

        ErrorResponse errorResponse = expenseClient.getById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Expense not Founded. Id: " + id, errorResponse.getMessage());
    }
}

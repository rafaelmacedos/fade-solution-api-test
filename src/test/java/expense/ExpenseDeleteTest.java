package expense;

import client.ExpenseClient;
import dataFactory.ExpenseDataFactory;
import model.ErrorResponse;
import model.Expense;
import org.apache.http.HttpStatus;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpenseDeleteTest {
    private final ExpenseClient expenseClient = new ExpenseClient();

    @Test
    @DisplayName("Expense deletion by valid id test with success")
    @Description("This test attempts to delete an already saved expense.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteExpenseByIdWithSuccess() {
        Expense expense = ExpenseDataFactory.getExpenseFromAPI();

        expenseClient.deleteById(expense.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Expense deletion by invalid ID test without success")
    @Description("This test attempts to delete an already saved expense using an invalid Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteExpenseByIdWithInvalidIdWithoutSuccess() {
        String id = ExpenseDataFactory.getInvalidId();

        ErrorResponse errorResponse = expenseClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Expense not Founded. Id: " + id, errorResponse.getMessage());
    }

    @Test
    @DisplayName("Expense deletion by empty ID test without success")
    @Description("This test attempts to delete an already saved expense using an empty Id.")
    @Severity(SeverityLevel.NORMAL)
    void shouldDeleteExpenseByIdWithEmptyIdWithoutSuccess() {
        String id = ExpenseDataFactory.getEmptyId();

        ErrorResponse errorResponse = expenseClient.deleteById(id)
                .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract()
                        .as(ErrorResponse.class);

        assertEquals("Entity Expense not Founded. Id: " + id, errorResponse.getMessage());
    }
}

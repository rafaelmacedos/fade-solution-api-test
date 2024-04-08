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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ExpensePutTest {
    private final ExpenseClient expenseClient = new ExpenseClient();
    private final ZoneId zoneId = ZoneId.systemDefault();

    @Test
    @DisplayName("Update a Expense by valid ID using valid data with success")
    @Description("This test attempts to update an existent expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateExpenseWithSuccess() {
        Expense Expense = ExpenseDataFactory.getExpenseWithUpdatedData();

        Expense putResponse = expenseClient.put(Expense, Expense.getId())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Expense.class);


        LocalDate createdAtToLocalDate = LocalDate.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate today = LocalDate.now();

        LocalDateTime createdAt = LocalDateTime.parse(putResponse.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assertAll("Grouped Expense Assertions",
                () -> assertEquals(Expense.getId(), putResponse.getId()),
                () -> assertEquals(Expense.getBarberId(), putResponse.getBarberId()),
                () -> assertEquals(Expense.getDescription(), putResponse.getDescription()),
                () -> assertEquals(Expense.getExpenseType(), putResponse.getExpenseType()),
                () -> assertEquals(Expense.getTotalValue(), putResponse.getTotalValue()),
                () -> assertEquals(today, createdAtToLocalDate),
                () -> assertNotEquals(createdAt, updatedAt.atZone(zoneId).toLocalDateTime())
        );
    }

    @Test
    @DisplayName("Update a Expense by invalid ID using valid data without success")
    @Description("This test attempts to update an existent expense in the database.")
    @Severity(SeverityLevel.NORMAL)
    void shouldUpdateExpenseWithInvalidIdWithoutSuccess() {
        Expense Expense = ExpenseDataFactory.getExpenseWithUpdatedData();
        Expense.setId(null);
        String id = ExpenseDataFactory.getInvalidId();

        ErrorResponse errorResponse = expenseClient.put(Expense, id)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .as(ErrorResponse.class);

        assertEquals("Entity Expense not Founded. Id: " + id, errorResponse.getMessage());
    }
}

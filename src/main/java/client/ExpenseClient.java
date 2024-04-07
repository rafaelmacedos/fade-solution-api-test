package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.Expense;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class ExpenseClient extends BaseClient {
    private static final String EXPENSES = "expenses";
    private static final String TOKEN = Auth.tokenAdmin();

    public ExpenseClient() {
        BaseClient.initConfig();
    }

    public Response post(Expense expense) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(expense)
                        .when()
                            .post(EXPENSES)
                ;
    }

    public Response put(Expense expense, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(expense)
                        .when()
                            .put(EXPENSES + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(EXPENSES + "/all")
                ;
    }

    public Response getById(String expenseId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", expenseId)
                        .when()
                            .get(EXPENSES + "/byId/{id}")
                ;
    }

    public Response deleteById(String expenseId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", expenseId)
                        .when()
                            .delete(EXPENSES + "/{id}")
                ;
    }

}

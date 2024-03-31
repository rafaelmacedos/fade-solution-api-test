package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.Customer;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class CustomerClient extends BaseClient {
    private static final String CUSTOMERS = "customers";
    private static final String TOKEN = Auth.tokenAdmin();

    public CustomerClient() {
        BaseClient.initConfig();
    }

    public Response post(Customer customer) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(customer)
                        .when()
                            .post(CUSTOMERS)
                ;
    }

    public Response put(Customer customer, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(customer)
                        .when()
                            .put(CUSTOMERS + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(CUSTOMERS + "/all")
                ;
    }

    public Response getById(String customerId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", customerId)
                        .when()
                            .get(CUSTOMERS + "/byId/{id}")
                ;
    }

    public Response deleteById(String customerId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", customerId)
                        .when()
                            .delete(CUSTOMERS + "/{id}")
                ;
    }

}

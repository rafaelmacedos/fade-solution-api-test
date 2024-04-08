package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.Sale;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class SaleClient extends BaseClient {
    private static final String SALES = "sales";
    private static final String TOKEN = Auth.tokenAdmin();

    public SaleClient() {
        BaseClient.initConfig();
    }

    public Response post(Sale Sale) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(Sale)
                        .when()
                            .post(SALES)
                ;
    }

    public Response put(Sale Sale, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(Sale)
                        .when()
                            .put(SALES + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(SALES + "/all")
                ;
    }

    public Response getById(String SaleId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", SaleId)
                        .when()
                            .get(SALES + "/byId/{id}")
                ;
    }

    public Response deleteById(String SaleId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", SaleId)
                        .when()
                            .delete(SALES + "/{id}")
                ;
    }

}

package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.Product;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class ProductClient extends BaseClient {
    private static final String PRODUCTS = "products";
    private static final String TOKEN = Auth.tokenAdmin();

    public ProductClient() {
        BaseClient.initConfig();
    }

    public Response post(Product product) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(product)
                        .when()
                            .post(PRODUCTS)
                ;
    }

    public Response put(Product product, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(product)
                        .when()
                            .put(PRODUCTS + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(PRODUCTS + "/all")
                ;
    }

    public Response getById(String productId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", productId)
                        .when()
                            .get(PRODUCTS + "/byId/{id}")
                ;
    }

    public Response deleteById(String productId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", productId)
                        .when()
                            .delete(PRODUCTS + "/{id}")
                ;
    }

}

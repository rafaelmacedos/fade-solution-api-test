package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.OfferedService;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class OfferedServiceClient extends BaseClient {
    private static final String SERVICES = "services";
    private static final String TOKEN = Auth.tokenAdmin();

    public OfferedServiceClient() {
        BaseClient.initConfig();
    }

    public Response post(OfferedService OfferedService) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(OfferedService)
                        .when()
                        .post(SERVICES)
                ;
    }

    public Response put(OfferedService OfferedService, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(OfferedService)
                        .when()
                        .put(SERVICES + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(SERVICES + "/all")
                ;
    }

    public Response getById(String OfferedServiceId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", OfferedServiceId)
                        .when()
                        .get(SERVICES + "/byId/{id}")
                ;
    }

    public Response deleteById(String OfferedServiceId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", OfferedServiceId)
                        .when()
                        .delete(SERVICES + "/{id}")
                ;
    }

}

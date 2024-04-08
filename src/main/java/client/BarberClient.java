package client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Barber;
import utils.Auth;

import static io.restassured.RestAssured.given;

public class BarberClient extends BaseClient {
    private static final String BARBERS = "barbers";
    private static final String TOKEN = Auth.tokenAdmin();

    public BarberClient() {
        BaseClient.initConfig();
    }

    public Response post(Barber barber) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(barber)
                        .when()
                            .post(BARBERS)
                ;
    }

    public Response put(Barber barber, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(barber)
                        .when()
                            .put(BARBERS + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(BARBERS + "/all")
                ;
    }

    public Response getById(String barberId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", barberId)
                        .when()
                            .get(BARBERS + "/byId/{id}")
                ;
    }

    public Response deleteById(String barberId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", barberId)
                        .when()
                            .delete(BARBERS + "/{id}")
                ;
    }

}

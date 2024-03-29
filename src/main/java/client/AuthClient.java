package client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Authentication;

import static io.restassured.RestAssured.*;

public class AuthClient extends BaseClient {
    private static final String AUTH_LOGIN = "auth/login";

    public AuthClient() {
        BaseClient.initConfig();
    }

    public Response login(Authentication authentication) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(authentication)
                        .when()
                            .post(AUTH_LOGIN)
                ;
    }
}
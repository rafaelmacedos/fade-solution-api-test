package client;

import static io.restassured.RestAssured.*;
public class BaseClient {
    public static void initConfig() {
        baseURI = "https://fade-solutions-server-production.up.railway.app/";
        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

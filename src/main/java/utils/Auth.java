package utils;

import client.AuthClient;
import model.Authentication;

public class Auth {
    private static final AuthClient authClient = new AuthClient();
    private static final String email = System.getenv("API_ADMIN_EMAIL");
    private static final String password = System.getenv("API_ADMIN_PASSWORD");

    public static String tokenAdmin() {
        Authentication adminLoginData = new Authentication(
                email,
                password
        );

        return authClient.login(adminLoginData)
                .then()
                .extract()
                .path("token")
                ;
    }
}
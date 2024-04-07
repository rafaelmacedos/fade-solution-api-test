package client;

import io.restassured.http.ContentType;
import io.restassured.response.*;
import model.Appointment;
import utils.Auth;

import static io.restassured.RestAssured.*;

public class AppointmentClient extends BaseClient {
    private static final String APPOINTMENTS = "appointments";
    private static final String TOKEN = Auth.tokenAdmin();

    public AppointmentClient() {
        BaseClient.initConfig();
    }

    public Response post(Appointment appointment) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .body(appointment)
                        .when()
                            .post(APPOINTMENTS)
                ;
    }

    public Response put(Appointment appointment, String id) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", id)
                        .body(appointment)
                        .when()
                            .put(APPOINTMENTS + "/{id}")
                ;
    }

    public Response getAll() {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .when()
                            .get(APPOINTMENTS + "/all")
                ;
    }

    public Response getById(String appointmentId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", appointmentId)
                        .when()
                            .get(APPOINTMENTS + "/byId/{id}")
                ;
    }

    public Response deleteById(String appointmentId) {
        return
                given()
                        .header("Authorization", TOKEN)
                        .contentType(ContentType.JSON)
                        .pathParam("id", appointmentId)
                        .when()
                            .delete(APPOINTMENTS + "/{id}")
                ;
    }

}

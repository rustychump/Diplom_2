package requests;

import cards.CreateUserCard;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderApi {
    UserApi userApi = new UserApi();
    public static final String ENDPOINT_ORDERS = "/api/orders";

    public Response orderCreateWithAuth(CreateUserCard userCard, File orderBody) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(userApi.getResponseAuthUserCard(userCard).getAccessToken().substring(7))
                .body(orderBody)
                .when()
                .post(ENDPOINT_ORDERS);
    }

    public Response orderCreateWithoutAuth(File orderBody) {
        return given()
                .header("Content-type", "application/json")
                .body(orderBody)
                .when()
                .post(ENDPOINT_ORDERS);
    }

    public Response getUserOrderWithoutAuth() {
        return given()
                .get(ENDPOINT_ORDERS);
    }

    public Response getUserOrderWithAuth(CreateUserCard userCard) {
        return given()
                .auth().oauth2(userApi.getResponseAuthUserCard(userCard).getAccessToken().substring(7))
                .when()
                .get(ENDPOINT_ORDERS);
    }
}

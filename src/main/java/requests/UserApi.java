package requests;

import cards.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {
    public static final String ENDPOINT_AUTH_LOGIN = "/api/auth/login";
    public static final String ENDPOINT_AUTH_REGISTER = "/api/auth/register";
    public static final String ENDPOINT_AUTH_USER = "/api/auth/user";

    protected ResponseAuthUserCard getResponseAuthUserCard(CreateUserCard userCard) {
        return userLogin(userCard).body().as(ResponseAuthUserCard.class);
    }

    public Response userLogin(CreateUserCard userCard) {
        return given()
                .header("Content-type", "application/json")
                .body(userCard)
                .when()
                .post(ENDPOINT_AUTH_LOGIN);
    }

    public Response userRegister(CreateUserCard userCard) {
        return given()
                .header("Content-type", "application/json")
                .body(userCard)
                .when()
                .post(ENDPOINT_AUTH_REGISTER);
    }

    public Response userRegister(CreateUserWithoutEmailCard userWithoutEmailCard) {
        return given()
                .header("Content-type", "application/json")
                .body(userWithoutEmailCard)
                .when()
                .post(ENDPOINT_AUTH_REGISTER);
    }

    public Response userRegister(CreateUserWithoutPasswordCard userWithoutPasswordCard) {
        return given()
                .header("Content-type", "application/json")
                .body(userWithoutPasswordCard)
                .when()
                .post(ENDPOINT_AUTH_REGISTER);
    }

    public Response userRegister(CreateUserWithoutNameCard userWithoutNameCard) {
        return given()
                .header("Content-type", "application/json")
                .body(userWithoutNameCard)
                .when()
                .post(ENDPOINT_AUTH_REGISTER);
    }

    public Response userDelete(CreateUserCard userCard) {
        return given()
                .auth().oauth2(getResponseAuthUserCard(userCard).getAccessToken().substring(7))
                .delete(ENDPOINT_AUTH_USER);
    }

    public Response userUpdateWithAuth(CreateUserCard userCard, CreateUserCard changedUserCard) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(getResponseAuthUserCard(userCard).getAccessToken().substring(7))
                .body(changedUserCard)
                .when()
                .patch(ENDPOINT_AUTH_USER);
    }

    public Response userUpdateWithoutAuth(CreateUserCard changedUserCard) {
        return given()
                .header("Content-type", "application/json")
                .body(changedUserCard)
                .when()
                .patch(ENDPOINT_AUTH_USER);
    }
}

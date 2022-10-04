import cards.CreateUserCard;
import cards.ResponseAuthUserCard;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class TestLoginUser {

    CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post("/api/auth/register");
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void correctUserLogin() {

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post("/api/auth/login")
                .then().statusCode(200)
                .and().assertThat().body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginUserWithIncorrectEmailAndPassword() {

        CreateUserCard createWrongUserCard = new CreateUserCard("wrong@yandex.ru", "wrong", "username");

        given()
                .header("Content-type", "application/json")
                .body(createWrongUserCard)
                .when()
                .post("/api/auth/login")
                .then().statusCode(401)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteTestData() {
        try {
            ResponseAuthUserCard responseAuthUserCard = given()
                    .header("Content-type", "application/json")
                    .body(createUserCard)
                    .when()
                    .post("/api/auth/login")
                    .body().as(ResponseAuthUserCard.class);

            given()
                    .auth().oauth2(responseAuthUserCard.getAccessToken().substring(7))
                    .delete("/api/auth/user");
        } catch (NullPointerException exception) { }
    }
}

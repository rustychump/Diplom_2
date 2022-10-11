import cards.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class TestCreateUser {

    CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createCorrectUser() {

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post("/api/auth/register")
                .then().statusCode(200)
                .and().assertThat().body("success", equalTo(true))
                .body("user", notNullValue())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void recreateUser() {
        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post("/api/auth/register");

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя, не передавая Email")
    public void createUserWithoutEmail() {
        CreateUserWithoutEmailCard createUserWithoutEmailCard = new CreateUserWithoutEmailCard("password", "username");

        given()
                .header("Content-type", "application/json")
                .body(createUserWithoutEmailCard)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя, не передавая пароль")
    public void createUserWithoutPassword() {
        CreateUserWithoutPasswordCard createUserWithoutPasswordCard = new CreateUserWithoutPasswordCard("matest@yandex.ru", "username");

        given()
                .header("Content-type", "application/json")
                .body(createUserWithoutPasswordCard)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя, не передавая имя")
    public void createUserWithoutName() {
        CreateUserWithoutNameCard createUserWithoutNameCard = new CreateUserWithoutNameCard("matest@yandex.ru", "password");

        given()
                .header("Content-type", "application/json")
                .body(createUserWithoutNameCard)
                .when()
                .post("/api/auth/register")
                .then().statusCode(403)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
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

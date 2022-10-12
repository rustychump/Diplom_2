import cards.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class TestCreateUser extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createCorrectUser() {

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post(endpointAuthRegister)
                .then().statusCode(SC_OK)
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
                .post(endpointAuthRegister);

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post(endpointAuthRegister)
                .then().statusCode(SC_FORBIDDEN)
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
                .post(endpointAuthRegister)
                .then().statusCode(SC_FORBIDDEN)
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
                .post(endpointAuthRegister)
                .then().statusCode(SC_FORBIDDEN)
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
                .post(endpointAuthRegister)
                .then().statusCode(SC_FORBIDDEN)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Override
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}

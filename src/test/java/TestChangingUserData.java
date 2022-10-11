import cards.CreateUserCard;
import cards.ResponseAuthUserCard;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestChangingUserData {

    CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");
    CreateUserCard changedUserCard = new CreateUserCard("matest1@yandex.ru", "password1", "username1");



    private ResponseAuthUserCard getResponseAuthUserCard(CreateUserCard userCard) {

        return given()
                .header("Content-type", "application/json")
                .body(userCard)
                .when()
                .post("/api/auth/login")
                .body().as(ResponseAuthUserCard.class);
    }

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
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changingUserDataWithAuth() {

        given()
                .header("Content-type", "application/json")
                .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                .body(changedUserCard)
                .when()
                .patch("/api/auth/user")
                .then().statusCode(200)
                .and().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo("matest1@yandex.ru"))
                .body("user.name", equalTo("username1"));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changingUserDataWithoutAuth() {

        given()
                .header("Content-type", "application/json")
                .body(changedUserCard)
                .when()
                .patch("/api/auth/user")
                .then().statusCode(401)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteTestData() {
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                    .delete("/api/auth/user");
        } catch (NullPointerException exception) { }
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(changedUserCard).getAccessToken().substring(7))
                    .delete("/api/auth/user");

        } catch (NullPointerException exception) { }
    }

}

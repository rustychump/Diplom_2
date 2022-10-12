import cards.CreateUserCard;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class TestChangingUserData extends BaseTest {
    CreateUserCard changedUserCard = new CreateUserCard("matest1@yandex.ru", "password1", "username1");
    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changingUserDataWithAuth() {

        given()
                .header("Content-type", "application/json")
                .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                .body(changedUserCard)
                .when()
                .patch(endpointAuthUser)
                .then().statusCode(SC_OK)
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
                .patch(endpointAuthUser)
                .then().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Override
    public void deleteTestData() {
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                    .delete(endpointAuthUser);
        } catch (NullPointerException exception) { }
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(changedUserCard).getAccessToken().substring(7))
                    .delete(endpointAuthUser);

        } catch (NullPointerException exception) { }
    }

}

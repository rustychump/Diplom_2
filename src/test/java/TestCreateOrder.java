import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCreateOrder extends BaseTest {
    File correctOrderBody = new File("src/main/resources/correctOrder.json");
    File incorrectOrderBody = new File("src/main/resources/incorrectOrder.json");

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингридиентами")
    public void createOrderWithAuth() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                .body(correctOrderBody)
                .when()
                .post(ENDPOINT_ORDERS)
                .then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .body("order.owner.name", equalTo("username"))
                .body("order.owner.email", equalTo("matest@yandex.ru"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        given()
                .header("Content-type", "application/json")
                .body(correctOrderBody)
                .when()
                .post(ENDPOINT_ORDERS)
                .then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    public void createOrderWithoutIngredients() {

        given()
                .header("Content-type", "application/json")
                .body("")
                .when()
                .post(ENDPOINT_ORDERS)
                .then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientHash() {
        given()
                .header("Content-type", "application/json")
                .body(incorrectOrderBody)
                .when()
                .post(ENDPOINT_ORDERS)
                .then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}

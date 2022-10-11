import cards.CreateUserCard;
import cards.ResponseAuthUserCard;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCreateOrder {

    CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");
    File correctOrderBody = new File("src/main/resources/correctOrder.json");
    File incorrectOrderBody = new File("src/main/resources/incorrectOrder.json");

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
    @DisplayName("Создание заказа с авторизацией и с ингридиентами")
    public void createOrderWithAuth() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                .body(correctOrderBody)
                .when()
                .post("/api/orders")
                .then().statusCode(200)
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
                .post("/api/orders")
                .then().statusCode(200)
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
                .post("/api/orders")
                .then().statusCode(400)
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
                .post("/api/orders")
                .then().statusCode(500);
    }

    @After
    public void deleteTestData() {
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                    .delete("/api/auth/user");
        } catch (NullPointerException exception) { }
    }
}

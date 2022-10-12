import cards.CreateUserCard;
import cards.ResponseAuthUserCard;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {

    public static final String ENDPOINT_AUTH_USER = "/api/auth/user";
    public static final String ENDPOINT_AUTH_LOGIN = "/api/auth/login";
    public static final String ENDPOINT_AUTH_REGISTER = "/api/auth/register";
    public static final String ENDPOINT_ORDERS = "/api/orders";
    protected CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");

    protected ResponseAuthUserCard getResponseAuthUserCard(CreateUserCard userCard) {

        return given()
                .header("Content-type", "application/json")
                .body(userCard)
                .when()
                .post(ENDPOINT_AUTH_LOGIN)
                .body().as(ResponseAuthUserCard.class);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post(ENDPOINT_AUTH_REGISTER);
    }

    @After
    public void deleteTestData() {
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                    .delete(ENDPOINT_AUTH_USER);
        } catch (NullPointerException exception) { }
    }
}

import cards.CreateUserCard;
import cards.ResponseAuthUserCard;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {
    public static final String endpointAuthUser = "/api/auth/user";
    public static final String endpointAuthLogin = "/api/auth/login";
    public static final String endpointAuthRegister = "/api/auth/register";
    public static final String endpointOrders = "/api/orders";
    protected CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");

    protected ResponseAuthUserCard getResponseAuthUserCard(CreateUserCard userCard) {

        return given()
                .header("Content-type", "application/json")
                .body(userCard)
                .when()
                .post(endpointAuthLogin)
                .body().as(ResponseAuthUserCard.class);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        given()
                .header("Content-type", "application/json")
                .body(createUserCard)
                .when()
                .post(endpointAuthRegister);
    }

    @After
    public void deleteTestData() {
        try {
            given()
                    .auth().oauth2(getResponseAuthUserCard(createUserCard).getAccessToken().substring(7))
                    .delete(endpointAuthUser);
        } catch (NullPointerException exception) { }
    }
}

import cards.CreateUserCard;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import requests.OrderApi;
import requests.UserApi;

public abstract class BaseTest {
    protected UserApi userApi = new UserApi();
    protected OrderApi orderApi = new OrderApi();
    protected CreateUserCard createUserCard = new CreateUserCard("matest@yandex.ru", "password", "username");
    protected static final String URL_STELLAR_BURGERS = "https://stellarburgers.nomoreparties.site";

    @Before
    public void setUp() {
        RestAssured.baseURI = URL_STELLAR_BURGERS;
        userApi.userRegister(createUserCard);
    }

    @After
    public void deleteTestData() {
        try {
            userApi.userDelete(createUserCard);
        } catch (NullPointerException exception) { }
    }
}

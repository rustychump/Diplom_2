import cards.CreateUserCard;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class TestLoginUser extends BaseTest {

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void correctUserLogin() {

        userApi.userLogin(createUserCard)
                .then().statusCode(SC_OK)
                .and().assertThat().body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginUserWithIncorrectEmailAndPassword() {

        CreateUserCard createWrongUserCard = new CreateUserCard("wrong@yandex.ru", "wrong", "username");

        userApi.userLogin(createWrongUserCard)
                .then().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}

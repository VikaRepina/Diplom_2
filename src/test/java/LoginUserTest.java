import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class LoginUserTest {
    private static final String name = "Usernamea";
    private static final String email = "testaaaa-data@yandex.ru";
    private static final String password = "paaaasswordaa";
    private static final String emailN = "ttestaa-data@yandex.ru";
    private static final String passwordN = "paasswordaa";
    private String Token;
    private final Gson gson = new Gson();

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        if (Token != null) {
            CreateUserApi createUserApi = new CreateUserApi();
            Response response = createUserApi.deleteUser(Token);
            response.then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Test authorization with existing login")
    @Description("Проверка авторизации под существующим пользователем")
    public void testAuthorizationWithExistingLogin() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        responseSecond.then().statusCode(200);
    }

    @Test
    @DisplayName("Test authorization with non-existent login")
    @Description("Проверка авторизации с неверным логином и паролем")
    public void testAuthorizationWithNonExistentLogin() {
        User user = new User(name, emailN, passwordN);
        LoginApi loginApi = new LoginApi();
        Response response = loginApi.authorizationWithExistingLogin(user);
        response.then().statusCode(401);
    }
}

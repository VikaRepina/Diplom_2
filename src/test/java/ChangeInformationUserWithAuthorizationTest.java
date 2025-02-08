import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ChangeInformationUserWithAuthorizationTest {
    private static final String name = "Usernameaa";
    private static final String email = "testaa-data@yandex.ru";
    private static final String password = "passwordaa";
    private String nameC;
    private String emailC;
    private String passwordC;
    private String Token;
    private final Gson gson = new Gson();

    public ChangeInformationUserWithAuthorizationTest (String nameC, String emailC, String passwordC) {
        this.nameC = nameC;
        this.emailC = emailC;
        this.passwordC = passwordC;
    }

    @Before
    @Step("Создание пользователя и авторизация")
    public void setUp() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        Token = responseSecond.jsonPath().get("accessToken");
    }

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        if (Token != null) {
            CreateUserApi createUserApi = new CreateUserApi();
            Response response = createUserApi.deleteUser(Token);
            response.then().statusCode(202);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Useeeername", "testaa-data@yandex.ru", "passwordaa"},
                {"Usernameaa", "teeeesttaa-data@yandex.ru", "passwordaa"},
                {"Usernameaa", "testaa-data@yandex.ru", "paaaaasswoorda"},
        });
    }

    @Test
    @DisplayName("Test change information user with authorization")
    @Description("Проверка на изменение данных авторизованного пользователя")
    public void testChangeInformationUserWithAuthorization() {
       ChangeInformationUser changeInformationUser = new ChangeInformationUser();
       User updateUser = new User(nameC, emailC, passwordC);
       Response response = changeInformationUser.changeInformation(updateUser, Token);
       response.then().statusCode(200);
    }
}

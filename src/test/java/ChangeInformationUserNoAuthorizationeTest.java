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

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class ChangeInformationUserNoAuthorizationeTest {
    private static final String name = "Usernameaa";
    private static final String email = "testaa-data@yandex.ru";
    private static final String password = "passwordaa";
    private String nameC;
    private String emailC;
    private String passwordC;
    private String Token;
    private String refreshToken;
    private final Gson gson = new Gson();

    public ChangeInformationUserNoAuthorizationeTest (String nameC, String emailC, String passwordC) {
        this.nameC = nameC;
        this.emailC = emailC;
        this.passwordC = passwordC;
    }

    @Before
    @Step("Создание пользователя и выход из ситемы")
    public void setUp() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        Token = response.jsonPath().get("accessToken");
        refreshToken = response.jsonPath().get("refreshToken");

        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        Response responseSecond = changeInformationUser.leavingSystem(refreshToken);
        responseSecond.then().statusCode(200);
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

    @Parameterized.Parameters(name = "Имя: {0}, Почта: {1}, Пароль: {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Useeeername", "testaa-data@yandex.ru", "passwordaa"},
                {"Usernameaa", "teeeesttaa-data@yandex.ru", "passwordaa"},
                {"Usernameaa", "testaa-data@yandex.ru", "paaaaasswoorda"},
        });
    }

    @Test
    @DisplayName("Test change information user no authorization")
    @Description("Проверка на изменение данных неавторизованного пользователя")
    public void testChangeInformationUserNoAuthorization() {
        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        User updateUser = new User(nameC, emailC, passwordC);
        Response response = changeInformationUser.changeInformationNoAuthorization(updateUser);
        response.then().statusCode(401);
        response.then().body("success", equalTo(false));
        response.then().body("message", equalTo("You should be authorised"));
    }
}

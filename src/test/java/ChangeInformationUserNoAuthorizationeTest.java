import com.github.javafaker.Faker;
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
import java.util.List;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class ChangeInformationUserNoAuthorizationeTest {
    private static String name;
    private static String email;
    private static String password;
    private String nameC;
    private String emailC;
    private String passwordC;
    private String TokenA;
    private String token;
    private String refreshToken;
    private final Gson gson = new Gson();
    private static final Faker faker = new Faker();

    static {
        Faker faker = new Faker();
        name = faker.name().username();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
    }

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
        TokenA = response.jsonPath().get("accessToken");
        refreshToken = response.jsonPath().get("refreshToken");

        LogoutRequest logoutRequest = new LogoutRequest(token);
        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        Response responseSecond = changeInformationUser.leavingSystem(refreshToken);
        responseSecond.then().statusCode(200);
    }

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        if (TokenA != null) {
            CreateUserApi createUserApi = new CreateUserApi();
            Response response = createUserApi.deleteUser(TokenA);
            response.then().statusCode(202);
        }
    }

    @Parameterized.Parameters(name = "Имя: {0}, Почта: {1}, Пароль: {2}")
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String randomName = faker.name().username();
            String randomEmail = faker.internet().emailAddress();
            String randomPassword = faker.internet().password();
            data.add(new Object[]{randomName, randomEmail, randomPassword});
        }

        return data;
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

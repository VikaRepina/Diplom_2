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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class ChangeInformationUserWithAuthorizationTest {
    private String name;
    private String email;
    private String password;
    private static final String nameC = "Useeeername";
    private static final String emailC = "teeeesttaa-data@yandex.ru";
    private String Token;
    private final Gson gson = new Gson();
    private static final Faker faker = new Faker();

    {
        Faker faker = new Faker();
        name = faker.name().username();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
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

    @Test
    @DisplayName("Test change information user with authorization")
    @Description("Проверка на изменение данных авторизованного пользователя")
    public void testChangeInformationNameUserWithAuthorization() {
       ChangeInformationUser changeInformationUser = new ChangeInformationUser();
       User updateUser = new User(nameC, email, password);
       Response response = changeInformationUser.changeInformation(updateUser, Token);
       response.then().statusCode(200);
       response.then().body("success", equalTo(true));

       response.then().body("user.name", equalTo("Useeeername"));


    }

    @Test
    @DisplayName("Test change information user with authorization")
    @Description("Проверка на изменение данных авторизованного пользователя")
    public void testChangeInformationEmailUserWithAuthorization() {
        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        User updateUser = new User(name, emailC, password);
        Response response = changeInformationUser.changeInformation(updateUser, Token);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));

        response.then().body("user.email", equalTo("teeeesttaa-data@yandex.ru"));

    }
}

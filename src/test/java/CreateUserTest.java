import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest {
    private static final String name = "Usernameaa";
    private static final String email = "testaa-data@yandex.ru";
    private static final String password = "passwordaa";
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
    @DisplayName("Test create unique user")
    @Description("Проверка на успешное создание пользователя")
    public void testCreateUniqueUser () {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        response.then().statusCode(200);
        Token = response.jsonPath().get("accessToken");
    }

    @Test
    @DisplayName("Test create registered user")
    @Description("Проверка на вывод ошибки при создание пользователя, который уже зарегистрирован")
    public void testCreateRegisteredUser () {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        response.then().statusCode(200);
        Token = response.jsonPath().get("accessToken");

        Response responseSecond = createUserApi.createUser(user);
        responseSecond.then().statusCode(403);
    }
}

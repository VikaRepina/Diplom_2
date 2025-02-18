import com.github.javafaker.Faker;
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

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private static String name;
    private static String email;
    private static String password;
    private String Token;
    private final Gson gson = new Gson();
    private static final Faker faker = new Faker();

    static {
        Faker faker = new Faker();
        name = faker.name().username();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
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
        responseSecond.then().body("success", equalTo(false));
        responseSecond.then().body("message", equalTo("User already exists"));
    }
}

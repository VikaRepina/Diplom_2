import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private static final String name = "Usernameaa";
    private static final String email = "testaa-data@yandex.ru";
    private static final String password = "passwordaa";
    private static final String ingredients1 = "61c0c5a71d1f82001bdaaa6d";
    private static final String ingredients2 = "61c0c5a71d1f82001bdaaa6f";
    private static final String ingredients3 = "61c0c5a71d1f82001bdaaa70";
    private String Token;
    private String refreshToken;
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
    @DisplayName("Test create order with authorization end ingredients")
    @Description("Проверка на создание заказа c ингредиентами авторизованным пользователем")
    public void testCreateOrderWithAuthorizationEndIngredients() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        Token = responseSecond.jsonPath().get("accessToken");

        OrderApi orderApi = new OrderApi();
        Response responseThird = orderApi.createOrder(ingredients1, ingredients2, ingredients3, Token);
        responseThird.then().statusCode(200);
    }

    @Test
    @DisplayName("Test create order no authorization")
    @Description("Проверка на создание заказа неавторизованным пользователем")
    public void testCreateOrderNoAuthorization() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        Token = response.jsonPath().get("accessToken");


        OrderApi orderApi = new OrderApi();
        Response responseThird = orderApi.createOrderNoAuthorization(ingredients1, ingredients2, ingredients3);
        responseThird.then().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Test create order without ingredients")
    @Description("Проверка на создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        Token = responseSecond.jsonPath().get("accessToken");

        OrderApi orderApi = new OrderApi();
        Response responseThird = orderApi.createOrderWithoutIngredients(Token);
        responseThird.then().statusCode(400);
    }

    @Test
    @DisplayName("Test create order with invalid ingredient hash")
    @Description("Проверка на создание заказа с неверным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredientHash() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        Token = responseSecond.jsonPath().get("accessToken");

        OrderApi orderApi = new OrderApi();
        String invalidIngredient1 = "61c0gtahrh508yhdohataaa40";
        String invalidIngredient2 = "679a0c5a58d1f82001bdeda54";
        String invalidIngredient3 = "61c0c577ogFjstrj9rjjaaa94";
        Response responseThird = orderApi.createOrder(invalidIngredient1, invalidIngredient2, invalidIngredient3, Token);
        responseThird.then().statusCode(500);
    }
}

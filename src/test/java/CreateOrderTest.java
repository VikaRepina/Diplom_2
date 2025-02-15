import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private static final String name = "Usernameaa";
    private static final String email = "testaa-data@yandex.ru";
    private static final String password = "passwordaa";
    private static final String ingredients = "61c0c5a71d1f82001bdaaa6d";
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
        Order order = new Order(ingredients);
        Response responseThird = orderApi.createOrder(order, Token);
        responseThird.then().statusCode(200);
        responseThird.then().body("success", equalTo(true));
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
        Order order = new Order(ingredients);
        Response responseThird = orderApi.createOrderNoAuthorization(order);
        responseThird.then().statusCode(401);
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
        Order order = new Order(null);
        Response responseThird = orderApi.createOrderWithoutIngredients(order, Token);
        responseThird.then().statusCode(400);
        responseThird.then().body("success", equalTo(false));
        responseThird.then().body("message", equalTo("Ingredient ids must be provided"));
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
        String invalidIngredient = "61c0gtahrh508yhdohataaa40";
        Order invalidOrder = new Order(invalidIngredient);
        Response responseThird = orderApi.createOrder(invalidOrder, Token);
        responseThird.then().statusCode(500).body(containsString("Internal Server Error"));
    }
}

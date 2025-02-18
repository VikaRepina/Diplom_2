import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ReceiveUserOrdersTest {
    private static String name;
    private static String email;
    private static String password;
    private static final String ingredients = "61c0c5a71d1f82001bdaaa6d";
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

    @After
    @Step("Удаление созданного пользователя")
    public void tearDown() {
        if (TokenA != null) {
            CreateUserApi createUserApi = new CreateUserApi();
            Response response = createUserApi.deleteUser(TokenA);
            response.then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Test receive user orders with authorization")
    @Description("Проверка на получение заказов конкретного авторизованного пользователя")
    public void testReceiveUserOrdersWithAuthorization() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        TokenA = responseSecond.jsonPath().get("accessToken");

        OrderApi orderApi = new OrderApi();
        Order order = new Order(ingredients);
        Response responseThird = orderApi.createOrder(order, TokenA);
        Response responseFourth = orderApi.receiveUserOrders(TokenA);
        responseFourth.then().statusCode(200);
        responseFourth.then().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Test receive user orders no authorization")
    @Description("Проверка на получение заказов неавторизованного пользователя")
    public void testReceiveUserOrdersNoAuthorization() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        TokenA = response.jsonPath().get("accessToken");
        refreshToken = response.jsonPath().get("refreshToken");

        LogoutRequest logoutRequest = new LogoutRequest(token);
        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        Response responseSecond = changeInformationUser.leavingSystem(refreshToken);

        OrderApi orderApi = new OrderApi();
        Order order = new Order(ingredients);
        Response responseThird = orderApi.createOrder(order, TokenA);
        Response responseFourth = orderApi.receiveUserOrdersNoAuthorization();
        responseFourth.then().statusCode(401);
        responseFourth.then().body("success", equalTo(false));
        responseFourth.then().body("message", equalTo("You should be authorised"));

    }
}

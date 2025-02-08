import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ReceiveUserOrdersTest {
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
    @DisplayName("Test receive user orders with authorization")
    @Description("Проверка на получение заказов конкретного авторизованного пользователя")
    public void testReceiveUserOrdersWithAuthorization() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);

        LoginApi loginApi = new LoginApi();
        Response responseSecond = loginApi.authorizationWithExistingLogin(user);
        Token = responseSecond.jsonPath().get("accessToken");

        OrderApi orderApi = new OrderApi();
        Response responseThird = orderApi.createOrder(ingredients1, ingredients2, ingredients3, Token);
        Response responseFourth = orderApi.receiveUserOrders(Token);
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
        Token = response.jsonPath().get("accessToken");
        refreshToken = response.jsonPath().get("refreshToken");

        ChangeInformationUser changeInformationUser = new ChangeInformationUser();
        Response responseSecond = changeInformationUser.leavingSystem(refreshToken);

        OrderApi orderApi = new OrderApi();
        Response responseThird = orderApi.createOrder(ingredients1, ingredients2, ingredients3, Token);
        Response responseFourth = orderApi.receiveUserOrdersNoAuthorization();
        responseFourth.then().statusCode(401);
        responseFourth.then().body("success", equalTo(false));

    }
}

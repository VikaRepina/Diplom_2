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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CreateUserParametrizedTest {
    private String email;
    private String name;
    private String password;
    private final Gson gson = new Gson();

    public CreateUserParametrizedTest (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "testaa-data@yandex.ru", "passwordaa"},
                {"Usernameaa", "", "passwordaa"},
                {"Usernameaa", "testaa-data@yandex.ru", ""},
        });
    }

    @Test
    @DisplayName("Test courier with missing field")
    @Description("Проверка на вывод ошибки, при создание пользователя без обязательного поля")
    public void testCourierWithMissingField() {
        User user = new User(name, email, password);
        CreateUserApi createUserApi = new CreateUserApi();
        Response response = createUserApi.createUser(user);
        response.then().statusCode(403);
    }

}

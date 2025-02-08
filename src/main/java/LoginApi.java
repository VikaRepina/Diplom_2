import com.google.gson.Gson;
import io.restassured.response.Response;

public class LoginApi {
    private final Gson gson = new Gson();

    public Response authorizationWithExistingLogin(User user) {
        String requestBody = gson.toJson(user);
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/auth/login");
    }
}

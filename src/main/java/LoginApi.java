import com.google.gson.Gson;
import io.restassured.response.Response;

public class LoginApi {
    private final Gson gson = new Gson();

    public Response authorizationWithExistingLogin(User user) {
        return  ApiBase.getRequestSpecification()
                .body(user)
                .post("/api/auth/login");
    }
}

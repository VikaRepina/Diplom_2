import com.google.gson.Gson;
import io.restassured.response.Response;

public class CreateUserApi {
    private final Gson gson = new Gson();

    public Response createUser (User user) {
        String requestBody = gson.toJson(user);
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/auth/register");
    }

    public Response deleteUser(String Token) {
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .delete("/api/auth/user");
    }
}

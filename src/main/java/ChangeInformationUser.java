import com.google.gson.Gson;
import groovyjarjarantlr4.runtime.Token;
import io.restassured.response.Response;

public class ChangeInformationUser {
    private final Gson gson = new Gson();

    public Response changeInformation (User updateUser, String Token) {
        String requestBody = gson.toJson(updateUser);
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(requestBody)
                .patch("/api/auth/user");
    }

    public Response changeInformationNoAuthorization (User updateUser) {
        String requestBody = gson.toJson(updateUser);
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .patch("/api/auth/user");
    }

    public Response leavingSystem(String refreshToken) {
        String requestBody = "{ \"token\": \"" + refreshToken + "\" }";
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/auth/logout");
    }
}

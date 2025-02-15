import com.google.gson.Gson;
import groovyjarjarantlr4.runtime.Token;
import io.restassured.response.Response;

public class ChangeInformationUser {
    private final Gson gson = new Gson();

    public Response changeInformation (User updateUser, String Token) {
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(updateUser)
                .patch("/api/auth/user");
    }

    public Response changeInformationNoAuthorization (User updateUser) {
        return  ApiBase.getRequestSpecification()
                .body(updateUser)
                .patch("/api/auth/user");
    }

    public Response leavingSystem(String refreshToken) {
        String requestBody = "{ \"token\": \"" + refreshToken + "\" }";
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/auth/logout");
    }
}

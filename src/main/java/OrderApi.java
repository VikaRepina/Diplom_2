import com.google.gson.Gson;
import io.restassured.response.Response;

public class OrderApi {
    private final Gson gson = new Gson();

    public Response createOrder (String ingredients1, String ingredients2, String ingredients3, String Token) {
        String requestBody = "{ \"ingredients\": [\"" + ingredients1 + "\", \"" + ingredients2 + "\", \"" + ingredients3 + "\"] }";
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(requestBody)
                .post("/api/orders");
    }

    public Response createOrderNoAuthorization (String ingredients1, String ingredients2, String ingredients3) {
        String requestBody = "{ \"ingredients\": [\"" + ingredients1 + "\", \"" + ingredients2 + "\", \"" + ingredients3 + "\"] }";
        return  ApiBase.getRequestSpecification()
                .body(requestBody)
                .post("/api/orders");
    }

    public Response createOrderWithoutIngredients(String Token) {
        String requestBody = "{ \"ingredients\": [] }";
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(requestBody)
                .post("/api/orders");
    }

    public Response receiveUserOrders(String Token) {
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .get("/api/orders");
    }

    public Response receiveUserOrdersNoAuthorization() {
        return  ApiBase.getRequestSpecification()
                .get("/api/orders");
    }
}

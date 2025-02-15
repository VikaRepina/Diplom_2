import com.google.gson.Gson;
import io.restassured.response.Response;

public class OrderApi {
    private final Gson gson = new Gson();

    public Response createOrder (Order order, String Token) {
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(order)
                .post("/api/orders");
    }

    public Response createOrderNoAuthorization (Order order) {
        return  ApiBase.getRequestSpecification()
                .body(order)
                .post("/api/orders");
    }

    public Response createOrderWithoutIngredients(Order order, String Token) {
        return  ApiBase.getRequestSpecification()
                .header("Authorization", Token)
                .body(order)
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

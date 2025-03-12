package academits.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class PostCreateUsers {

    @Test
    public void createUser() {
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\n" +
                        "    \"name\": \"ivan\",\n" +
                        "    \"job\": \"cook\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/users")
                .andReturn();
        response.prettyPrint();
    }
}

package academits.reqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;

public class GetListResourseTest {

    @Test
    public void getListResourse() {

        Response response = RestAssured
                .get("https://reqres.in/api/unknown")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void testWithIncorrectUrl() {
        ValidatableResponse response = RestAssured
                .get("https://reqres.in/unknown")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void getListResourseWithParam() {

        ValidatableResponse response = RestAssured
                .given()
                .queryParam("page", "3")
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200);
        System.out.println("Content-Type: " + response.extract().header("Content-Type"));
        System.out.println("Response: \n" + response.extract().asPrettyString());
    }

    @Test
    public void getListResourseWithParamInMap() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page", "1");

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://reqres.in/api/unknown")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void getListResourseStatusCode() {

        Response response = RestAssured
                .get("https://reqres.in/api/unknown?page=2")
                .andReturn();
        response.prettyPrint();

        int statusCode = response.getStatusCode();
        System.out.println("Status code" + statusCode);

        Assertions.assertEquals(200, statusCode, "Unexpected status code: expected - 400, but was " + statusCode);
    }

    @Test
    public void testParseJson() {

        JsonPath response = RestAssured
                .given()
                .get("https://reqres.in/api/unknown?page=2")
                .jsonPath();
        response.prettyPrint();

        SoftAssertions softAssert = new SoftAssertions();

        softAssert.assertThat(response.get("page").toString()).isEqualTo("2");
        softAssert.assertThat(response.get("per_page").toString()).isEqualTo("6");
        softAssert.assertThat(response.get("total").toString()).isEqualTo("12");
        softAssert.assertThat(response.get("total_pages").toString()).isEqualTo("2");
        softAssert.assertThat(response.get("data").toString().length()).isNotEqualTo("0");
        softAssert.assertThat(response.get("data[2].name").toString()).isEqualTo("blue iris");

        softAssert.assertAll();
    }

    @Test
    public void testJsonKey() {

        Response response = RestAssured
                .get("https://reqres.in/api/unknown?page=2")
                .andReturn();
        response.prettyPrint();

        String[] expectedKeys = {"page", "per_page", "total", "total_pages", "data", "support"};

        for (String expectedKey : expectedKeys) {
            response.then().assertThat().body("$", hasKey(expectedKey));
        }
    }
}


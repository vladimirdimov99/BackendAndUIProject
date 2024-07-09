package backendTests.putRequests;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestBase;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class PutRequests extends TestBase {

    SoftAssert softAssert = new SoftAssert();

    @Test
    void updateSpecificUser() {

        HashMap credentials = new HashMap();
        credentials.put("name", "Stoyan");
        credentials.put("job", "Construction worker");

        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .pathParam("userID", 2)
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .put(properties.getProperty("backendURL") + "{usersPath}/{userID}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "The expected Status code doesn't match!!!");
        JSONObject jsonObject = new JSONObject(response.asString());
        String name = jsonObject.getString("name");
        String job = jsonObject.getString("job");

        softAssert.assertEquals(name, "Stoyan", "The updated expected name doesn't match!!!");
        softAssert.assertEquals(job, "Construction worker", "The updated expected job doesn't match!!!");
    }

    @AfterTest
    void softAssertAll() {
        softAssert.assertAll();
    }
}

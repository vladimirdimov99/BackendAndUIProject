package backendTests.deleteRequests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestBase;

import static io.restassured.RestAssured.*;

public class DeleteRequests extends TestBase {
    SoftAssert softAssert = new SoftAssert();

    @Test
    void deleteSpecificUser() {
        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .pathParam("userID", 2)
                        .when()
                        .delete(properties.getProperty("backendURL") + "{usersPath}/{userID}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 204);
        softAssert.assertAll();
    }
}

package backendTests.postRequests;

import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestBase;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class PostRequests extends TestBase {
    String access_token;
    SoftAssert softAssert = new SoftAssert();

    @Test
    void createUser() {

        HashMap credentials = new HashMap();
        credentials.put("name", "Vladimir");
        credentials.put("job", "Automation QA");

        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .post(properties.getProperty("backendURL") + "{usersPath}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 201);
        softAssert.assertEquals(response.jsonPath().getString("name"), "Vladimir", "The given name doesn't match!!!");
        softAssert.assertEquals(response.jsonPath().getString("job"), "Automation QA", "The given job doesn't match!!!");
    }

    @Test
    void registerSuccessfully() {

        HashMap credentials = new HashMap();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "pistol");

        Response response =
                given()
                        .pathParam("registerPath", "register")
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .post(properties.getProperty("backendURL") + "{registerPath}")
                        .then()
                        .log().body()
                        .extract().response();
        access_token = response.path("token").toString();

        softAssert.assertEquals(response.getStatusCode(), 200, "Status code is different!!!");
        softAssert.assertEquals(response.getContentType(), "application/json; charset=utf-8", "Content type is different!!!");
        softAssert.assertNotNull(response.jsonPath().getString("token"), "Access Token is with null value!!!");
    }

    @Test
    void registerWithoutPassword() {
        HashMap credentials = new HashMap();
        credentials.put("email", "sydney@fife");

        Response response =
                given()
                        .pathParam("registerPath", "register")
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .post(properties.getProperty("backendURL") + "{registerPath}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.getStatusCode(), 400, "Wrong Status code for Register without password!!!");
        softAssert.assertEquals(response.jsonPath().getString("error"), "Missing password", "There is no Missing password Error text!!!");
    }

    @Test(dependsOnMethods = {"registerSuccessfully"})
    void loginSuccessfully() {

        HashMap credentials = new HashMap();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "cityslicka");

        Response response =
                given()
                        .auth().oauth2(access_token)
                        .pathParam("loginPath", "login")
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .post(properties.getProperty("backendURL") + "{loginPath}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200);
        softAssert.assertEquals(response.jsonPath().getString("token"), access_token, "The Access Token doesn't match!!!");
    }

    @Test
    void loginWithMissingPassword() {

        HashMap credentials = new HashMap();
        credentials.put("email", "peter@klaven");

        Response response =
                given()
                        .pathParam("loginPath", "login")
                        .contentType("application/json")
                        .when()
                        .body(credentials)
                        .post(properties.getProperty("backendURL") + "{loginPath}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 400, "The Response code doesn't match!!!");
        softAssert.assertEquals(response.jsonPath().getString("error"), "Missing password", "There is not Missing password Error text!!!");
    }

    @AfterTest
    public void softAssertAll() {
        softAssert.assertAll();
    }
}
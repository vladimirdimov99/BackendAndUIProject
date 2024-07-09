package backendTests.getRequests;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestBase;

import java.util.List;

import static io.restassured.RestAssured.*;

public class GetRequests extends TestBase {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void getListOfUsers() {
        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .queryParam("page", 2)
                        .contentType("application/json; charset=utf-8")
                        .when()
                        .get(properties.getProperty("backendURL") + "{usersPath}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200);
        JSONObject jsonObject = new JSONObject(response.asString());
        int pageNum = jsonObject.getInt("page");
        softAssert.assertEquals(pageNum, 2);
    }

    @Test
    void getSingleUser() {

        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .pathParam("userID", 2)
                        .contentType("application/json")
                        .when()
                        .get(properties.getProperty("backendURL") + "{usersPath}/{userID}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "The Status code doesn't match!!!");
        JSONObject jsonObject = new JSONObject(response.asString());
        int id = jsonObject.getJSONObject("data").getInt("id");
        softAssert.assertEquals(id, 2, "The data ID doesn't match!!!");
    }

    @Test
    void getSingleUserNotFound() {

        Response response =
                given()
                        .pathParam("usersPath", "users")
                        .pathParam("userID", 23)
                        .contentType("application/json")
                        .when()
                        .get(properties.getProperty("backendURL") + "{usersPath}/{userID}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 404, "The Status code doesn't match!!!");
        softAssert.assertEquals(response.jsonPath().getString(""), "[:]", "The Response body is not only with curly braces");
    }

    @Test
    void getListOfUnknownUsers() {
        //Actually looks good but find another way
//        ArrayList<HashMap> array =
//                given()
//                        .pathParam("unknownPath", "unknown")
//                        .contentType("application/json")
//                        .when()
//                        .get(properties.getProperty("backendURL") + "{unknownPath}")
//                        .then()
//                        .statusCode(200)
//                        .extract().path("data");
//
//
//        for (HashMap data : array) {
//            if (data.containsKey("name") && data.containsValue("true red")) {
//                boolean isContaining = true;
//                System.out.println(isContaining);
//            }
//        }

        Response response =
                given()
                        .pathParam("unknownPath", "unknown")
                        .contentType("application/json")
                        .when()
                        .get(properties.getProperty("backendURL") + "{unknownPath}")
                        .then()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "The Status code doesn't match!!!");
        JSONObject jsonObject = new JSONObject(response.asString());
        String name = jsonObject.getJSONArray("data").getJSONObject(2).getString("name");
        softAssert.assertEquals(name, "true red", "The expected result name doesn't match!!!");
    }

    @Test
    void getSpecificUnknownUser() {

        Response response =
                given()
                        .pathParam("unknownPath", "unknown")
                        .pathParam("userID", 2)
                        .contentType("application/json")
                        .when()
                        .get(properties.getProperty("backendURL") + "{unknownPath}/{userID}")
                        .then()
                        .contentType("application/json; charset=utf-8")
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "The Status code doesn't match!!!");
        JSONObject jsonObject = new JSONObject(response.asString());
        int userID = jsonObject.getJSONObject("data").getInt("id");
        softAssert.assertEquals(userID, 2, "The expected user ID doesn't match!!!");
        String name = jsonObject.getJSONObject("data").getString("name");
        softAssert.assertEquals(name, "fuchsia rose", "The expected name doesn't match!!!");
    }

    @Test
    void getSingleNotFoundUnknownUser() {

        Response response =
                given()
                        .pathParam("unknownPath", "unknown")
                        .pathParam("userID", 23)
                        .contentType("application/json")
                        .when()
                        .get(properties.getProperty("backendURL") + "{unknownPath}/{userID}")
                        .then()
                        .log().body()
                        .extract().response();

        softAssert.assertEquals(response.statusCode(), 404, "The Status code doesn't match!!!");
        softAssert.assertEquals(response.jsonPath().getString(""), "[:]", "The Response body isn't with only curly braces!!!");
    }

    @Test
    void testXmlResponse() {
        Response response =
                given()
                        .when()
                        .get("https://thetestrequest.com/authors.xml")
                        .then()
                        .extract().response();


        XmlPath xmlPath = new XmlPath(response.asString());
        List<String> allObjects = xmlPath.getList("objects.object");
        softAssert.assertEquals(allObjects.size(), 5);

        List<String> names = xmlPath.getList("objects.object.name");
        boolean hasName = false;
        for (String data : names) {
            if (data.equals("Karl Zboncak")) {
                hasName = true;
                break;
            }
        }

        softAssert.assertEquals(hasName, true, "The given name doesn't exist!!!");
    }

    @AfterTest
    void softAssertAll() {
        softAssert.assertAll();
    }
}

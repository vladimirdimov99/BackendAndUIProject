package backendTests.schemaValidations;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;
import utils.TestBase;

import static io.restassured.RestAssured.given;

public class SchemaValidations extends TestBase {

    @Test
    void jsonSchemaValidation() {
        given()
                .pathParam("usersPath", "users")
                .queryParam("page", 2)
                .when()
                .get(properties.getProperty("backendURL") + "{usersPath}")
                .then()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchema.json"));
    }

    @Test
    void xmlSchemaValidation() {
        given()
                .when()
                .get("https://thetestrequest.com/authors.xml")
                .then()
                .assertThat().body(RestAssuredMatchers.matchesXsdInClasspath("xmlSchema.xsd"));
    }
}

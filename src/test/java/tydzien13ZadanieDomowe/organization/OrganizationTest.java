package tydzien13ZadanieDomowe.organization;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tydzien13ZadanieDomowe.baseForHomework.BaseForHomeworkTest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationTest extends BaseForHomeworkTest {
    private static String firstOrganizationId;
    private static String secondOrganizationId;
    private static String thirdOrganizationId;

    @Test
    @Order(1)
    public void creatFirstOrganizationWithHttpTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "First organization")
                .queryParam("desc", "Test")
                .queryParam("name", "test")
                .queryParam("website", "http://test.testowy.com")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("Created organization:\n" + response.prettyPrint());

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("First organization");
        assertThat(json.getString("desc")).isEqualTo("Test");
        assertThat(json.getString("name")).contains("test");
        assertThat(json.getString("website")).isEqualTo("http://test.testowy.com");

        firstOrganizationId = json.get("id");
        System.out.println(json.getString("id"));
    }

    @Test
    @Order(2)
    public void creatSecondOrganizationWithHttpsTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "Second organization")
                .queryParam("desc", "Test2")
                .queryParam("name", "test2")
                .queryParam("website", "https://test2.testowy2.com")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("Created organization:\n" + response.prettyPrint());

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Second organization");
        assertThat(json.getString("desc")).isEqualTo("Test2");
        assertThat(json.getString("name")).contains("test2");
        assertThat(json.getString("website")).isEqualTo("https://test2.testowy2.com");

        secondOrganizationId = json.get("id");
        System.out.println(json.getString("id"));
    }

    @Test
    @Order(3)
    public void creatFirstOrganizationWithEmptyDisplayNameTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    @Order(4)
    public void creatThirdOrganizationWithWrongNameTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "Third organization")
                .queryParam("name", "T")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("Created organization:\n" + response.prettyPrint());

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Third organization");
        assertThat(json.getString("name")).contains("t");

        thirdOrganizationId = json.get("id");
    }

    @Test
    @Order(5)
    public void deleteFirstOrganizationTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "Third organization")
                .queryParam("name", "T")
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + firstOrganizationId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    @Order(6)
    public void deleteSecondOrganizationTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "Third organization")
                .queryParam("name", "T")
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + secondOrganizationId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    @Order(7)
    public void deleteThirdOrganizationTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("displayName", "Third organization")
                .queryParam("name", "T")
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + thirdOrganizationId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}

package tydzien13ZadanieDomowe.e2e;

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
public class MoveCardBetweenListsHomeworkTest extends BaseForHomeworkTest {
    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String cardId;

    @Test
    @Order(1)
    public void createBoardTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "Test")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("name")).isEqualTo("Test");

        boardId = jsonPath.get("id");
    }

    @Test
    @Order(2)
    public void createFirstListTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("name", "First list")
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("name")).isEqualTo("First list");

        firstListId = jsonPath.get("id");
    }

    @Test
    @Order(3)
    public void createSecondtListTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("name", "Second list")
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("name")).isEqualTo("Second list");

        secondListId = jsonPath.get("id");
    }

    @Test
    @Order(4)
    public void createCardInFirstListTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idList", firstListId)
                .queryParam("name", "First card")
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("name")).isEqualTo("First card");

        cardId = jsonPath.get("id");
    }

    @Test
    @Order(5)
    public void moveCardToSecondListTest() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + CARDS + "/" + cardId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("idList")).isEqualTo(secondListId);
    }

    @Test
    @Order(6)
    public void deleteBoardTest() {
        Response response = given()
                .spec(reqSpecification)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}

package e2e;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//odpalanie testów pokoleji
public class MoveCardBetweenLists extends BaseTest {
    //https://github.com/bkita/stb_trello_demo

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String firstCardId;

    @Test
    @Order(1)//odpalanie testów pokoleji
    public void creatNewBoard() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "My e2e board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

//        dobrą praktyką jest sprządtanie po testach, utworzylismy board to go tez usowamy
        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My e2e board");
        boardId = json.get("id");
    }

    @Test
    @Order(2)
    public void createFirstList() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "My first List")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("My first List");
        firstListId = json.get("id");
    }

    @Test
    @Order(3)
    public void createSecondList() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "My second List")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("My second List");
        secondListId = json.get("id");
    }

    @Test
    @Order(4)
    public void addCardToFirstList() {
//        https://api.trello.com/1/cards
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idBoard", boardId)
                .queryParam("idList", firstListId)
                .queryParam("name", "My e2e card")
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My e2e card");
        firstCardId = json.get("id");
    }

    @Test
    @Order(5)
    public void moveCardToSecondList() {
//        https://api.trello.com/1/cards/{id}
        Response response = given()
                .spec(reqSpecification)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + CARDS + "/" + firstCardId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("idList")).isEqualTo(secondListId);
    }

    @Test
    @Order(6)
    public void deleteBoard() {
        given()
                .spec(reqSpecification)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}

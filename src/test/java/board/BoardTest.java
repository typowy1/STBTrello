package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest extends BaseTest {
    //https://github.com/bkita/stb_trello_demo

    @Test
    public void creatNewBoard() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "My second board")
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

//        dobrą praktyką jest sprządtanie po testach, utworzylismy board to go tez usowamy
        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("My second board");

        String boardId = json.get("id");
        given()
                .spec(reqSpecification)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);
    }

    @Test
    public void crateBoardWithEmptyBoardName() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createBoardWithoutDefaultLists() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "Board without default lists")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board without default lists");
//        System.out.println(response.prettyPrint()); //ładnie wyświetli jsona
//        https://api.trello.com/1/boards/{id}/lists
        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpecification)
                .when()
                .get(BASE_URL + BOARDS + "/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(responseGet.asString());
        JsonPath jsonGet = responseGet.jsonPath();
        List<String> idList = jsonGet.getList("id");
        assertThat(idList).hasSize(0);

        given()
                .spec(reqSpecification)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);
    }

    @Test
    public void createBoardWithDefaultLists() {
        Response response = given()
                .spec(reqSpecification)
                .queryParam("name", "Board with default lists")
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("name")).isEqualTo("Board with default lists");

        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpecification)
                .when()
                .get(BASE_URL + BOARDS + "/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(responseGet.prettyPrint());
        JsonPath jsonGet = responseGet.jsonPath();
        List<String> nameList = jsonGet.getList("name");
        assertThat(nameList).hasSize(3).contains("To Do", "Doing", "Done");

        given()
                .spec(reqSpecification)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(200);

    }
}

package tydzien13ZadanieDomowe.baseForHomework;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseForHomeworkTest {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATIONS = "organizations";

    protected static final String KEY = "3dce6f67a1fc2ee98c14c98e1e310569";
    protected static final String TOKEN = "773524badb9b285294a43350a4b640169c0f47cc729248cfd9251887527fd33e";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpecification;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType("application/json");

        reqSpecification = reqBuilder.build();
    }
}

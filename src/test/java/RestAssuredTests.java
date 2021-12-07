import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class RestAssuredTests {
    @BeforeAll
    public static void setup() {
        baseURI = "https://jsonplaceholder.typicode.com/";
    }

    public void divider() {
        System.out.println("\n====================================================\n");
    }

    public void title(String text) {
        System.out.println(ConsoleColors.BLUE_BOLD + text + ConsoleColors.RESET);
    }


    public void showResponseData(Response response) {
        System.out.println("Resnponse Data:");
        System.out.println("    Status Code: " + response.getStatusCode());
        System.out.println("    Time: " + response.getTime());
        System.out.println("    Body: \n" + response.getBody().asString());
        System.out.println("    " + response.getStatusLine());
        System.out.println("    " + response.getHeader("content-type"));
    }

    @Test
    public void
    checkStatusCode200() {
        title("Checando o retorno 200 da API");
        Response response = get();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200);
        System.out.println("    " + response.getStatusLine());
        divider();
    }


    @Test
    public void
    checkStatusCode500() {
        title("Checando o retorno 500 da API");
        Response response = post("?firstName=John&lastName=Doe");
        showResponseData(response);
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 500);
        divider();
    }

    @Test
    public void getSecondPost() {
        title("Checando o segundo post");
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/posts/2")
                .then()
                .extract().response();

        String body = """
                est rerum tempore vitae
                sequi sint nihil reprehenderit dolor beatae ea dolores neque
                fugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis
                qui aperiam non debitis possimus qui neque nisi nulla""";

        showResponseData(response);

        assertEquals(200, response.statusCode());
        assertEquals("qui est esse", response.jsonPath().getString("title"));
        assertEquals(2, response.jsonPath().getInt("id"));
        assertEquals(1, response.jsonPath().getInt("userId"));
        assertEquals(body, response.jsonPath().getString("body"));
        divider();
    }

    @Test
    public void postNewPost() {
        title("Checando método post nos posts ");
        String requestBody = """
                {
                  "title": "meu Todo",
                  "body": "Bom dia",
                  "userId": "1"\s
                }""";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .extract().response();

        showResponseData(response);

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("meu Todo", response.jsonPath().getString("title"));
        Assertions.assertEquals("Bom dia", response.jsonPath().getString("body"));
        Assertions.assertEquals("1", response.jsonPath().getString("userId"));
        Assertions.assertEquals("101", response.jsonPath().getString("id"));
        divider();
    }

    @Test
    public void putPost() {
        title("Checando método put nos posts");
        String requestBody = """
                {
                  "title": "Novo Titulo",
                  "body": "Novo Corpo",
                  "userId": "1",
                  "id": "1"\s
                }""";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/posts/1")
                .then()
                .extract().response();

        showResponseData(response);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Novo Titulo", response.jsonPath().getString("title"));
        Assertions.assertEquals("Novo Corpo", response.jsonPath().getString("body"));
        Assertions.assertEquals("1", response.jsonPath().getString("userId"));
        Assertions.assertEquals("1", response.jsonPath().getString("id"));
        divider();
    }

    @Test
    public void deletePost() {
        title("Checando delete put nos posts");
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/posts/1")
                .then()
                .extract().response();

        showResponseData(response);

        Assertions.assertEquals(200, response.statusCode());
        divider();
    }

}

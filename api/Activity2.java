package activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity2 {
    // Set base URL
    final static String ROOT_URI = "https://petstore.swagger.io/v2/user";

    @Test(priority=1)
    public void addNewUserFromFile() throws IOException {

        FileInputStream inputJSON = new FileInputStream("src/main/resources/User.json");
        String reqBody = new String(inputJSON.readAllBytes());

        Response response =
                given().contentType(ContentType.JSON)
                        .body(reqBody)
                        .when().post(ROOT_URI);

        inputJSON.close();

        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("8931"));
    }

    @Test(priority=2)
    public void getUserInfo() {
        // Import JSON file to write to
        File outputJSON = new File("src/main/resources/UserResponse.json");

        Response response =
                given().contentType(ContentType.JSON)
                        .pathParam("username", "Maverik")
                        .when().get(ROOT_URI + "/{username}");

        // Get response body
        String resBody = response.getBody().asPrettyString();

        try {
            // Create JSON file
            outputJSON.createNewFile();
            // Write response body to external file
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException excp) {
            excp.printStackTrace();
        }

        response.then().body("id", equalTo(8931));
        response.then().body("username", equalTo("Maverik"));
        response.then().body("firstName", equalTo("Martin"));
        response.then().body("lastName", equalTo("Charles"));
        response.then().body("email", equalTo("maverik@gmail.com"));
        response.then().body("password", equalTo("pass342"));
        response.then().body("phone", equalTo("9813423332"));
    }

    @Test(priority=3)
    public void deleteUser() throws IOException {
        Response response =
                given().contentType(ContentType.JSON)
                        .pathParam("username", "Maverik")
                        .when().delete(ROOT_URI + "/{username}");

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("Maverik"));
    }
}

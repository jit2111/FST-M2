package livProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GitHubProject {

    String baseURI = "https://api.github.com/user/keys";
    int id;
    RequestSpecification requestSpec;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","token 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")
                .build();
    }

    @Test(priority = 1)
    public void postRequest(){

        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC3meh+t8fal++4mPVKfW0vNEYHyDtwqnSWYv6p7ZdnwfZt4d8T7RGp47DyU6gAcYhlqfm5/mYnUib9qCoYOfDIwk8JrYcl3ATQa1ewR50+5uyfqwccaEwuRqwZsjXx4N87Uvgor9WGrfJteULqtf10CBbCViKY2JS5io61CFIT1dLSR5m2hwHilhYWSqIejBGQLNcFBq5+kPkeS/mDL9gNdaXgWMSwIBF+K/gt+1JIBm6gYLyingAMqc4DWFunKsBJloMDyYTo/zh7sV6aDQW8Qsr0mxItLqz5xCbNFquyYXr+3SjjvU/7mdercYXViXcU186PI2BNcuzzRocsFm1R\"}";

        Response res = given().spec(requestSpec)
                .body(reqBody).when().post();

//        System.out.println(res.getBody().asString());

        id = res.then().extract().path("id");
        res.then().statusCode(201);

    }

    @Test(priority = 2)
    public void getRequest(){
        Response res = given().spec(requestSpec)
                .pathParam("keyId",id)
                .when().get("/{keyId}");

        res.then().log().all().statusCode(200);
    }

    @Test(priority = 3)
    public void deleteRequest(){
        Response res = given().spec(requestSpec)
                .pathParam("keyId",id)
                .when().delete("/{keyId}");

        res.then().log().all().statusCode(204);
    }
}
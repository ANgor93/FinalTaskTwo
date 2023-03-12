package ApiSteps;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.cucumber.java.ru.*;
import io.qameta.allure.Allure;


import static ApiSpecification.ApiSpecification.getRequestSpecificationCreateUser;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;

public class UserCreateAPI {

    public static String requestBody;
    public static String responseName;
    public static String responseJob;


    @Дано("У меня есть запрос на создание пользователя")
    public static void setRequestBody() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/requestBody.json"))));
        body.put("name", "Tomato");
        body.put("job", "Eat market");
        requestBody = body.toString();
        String message = "Запрос на создание пользователя: " + requestBody;
        Allure.addAttachment("Request Body", message);
    }

    @Когда("Я отправляю POST запрос на endpoint {string}")
    public static void sendPostRequest(String endpoint) {
        Response response = given()
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .spec(getRequestSpecificationCreateUser())
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .statusCode(201)
                .extract()
                .response();

        responseName = response.jsonPath().getString("name");
        responseJob = response.jsonPath().getString("job");
        Allure.addAttachment("Response body", response.getBody().asString());

        Allure.addAttachment("Response name", responseName);
        Allure.addAttachment("Response job", responseJob);
    }

    @Тогда("Тело ответа должно содержать имя {string} и работу {string} созданного пользователя")
    public static void checkResponseBody(String expectedName, String expectedJob) {
        Assertions.assertEquals(expectedName, responseName);
        Assertions.assertEquals(expectedJob, responseJob);
        String attachmentName = "Response Body Values";
        String attachmentContent = String.format("Response name: %s, Response job: %s", responseName, responseJob);
        Allure.addAttachment(attachmentName, attachmentContent);
    }

}

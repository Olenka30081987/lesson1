package org.example;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ClassifyCuisineTests {

    private  String apiKey = "3016bd7643da41f6a68f3f55a1aabfd0";
    private  String url = "https://api.spoonacular.com/recipes/cuisine";

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    void postRequestPositiveTest3Param() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .formParam("title","sushi")
                .formParam("ingredientList","rice")
                .formParam("language","en")
                .post(url)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString("Japanese"))
                .body("confidence", equalTo (0.85F))
                .time(lessThan(3000L));
    }

    @Test
    void getRequestPositiveTest2Param() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .formParam("title","Bruschetta Style Pork & Pasta")
                .formParam("ingredientList","Pasta and Rice")
                .post(url)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString("Italian"))
                .time(lessThan(3000L));
    }

    @Test
    void getRequestPositiveTest1Param() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .formParam("title","tiramisu")
                .post(url)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString("Mediterranean"))
                .body("confidence", equalTo (0.85F))
                .time(lessThan(3000L));
    }

    @Test
    void getRequestIngredientListSymbol() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .formParam("title","Cake Balls")
                .formParam("ingredientList","№;%:")
                .post(url)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("confidence", equalTo (0.0F))
                .time(lessThan(3000L));
    }

    @Test
    void getRequestTitleSymbol() {
        given()
                .queryParam("apiKey", apiKey)
                .when()
                .formParam("title","@##$%^")
                .formParam("ingredientList","cheese")
                .formParam("language","en")
                .post(url)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("confidence", equalTo (0.0F))
                .time(lessThan(3000L));
    }
}

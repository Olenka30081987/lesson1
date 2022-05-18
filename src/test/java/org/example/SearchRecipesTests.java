package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class SearchRecipesTests {

    private final String apiKey = "3016bd7643da41f6a68f3f55a1aabfd0";
    private final String url = "https://api.spoonacular.com/recipes/complexSearch";

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Test
    void getRequestPositive4ParamsTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("query","soup")
                .queryParams("diet","Ovo-Vegetarian")
                .queryParams("sort","calories")
                .queryParams("maxReadyTime","60")
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), containsString("Soup"));
        assertThat(response.get("totalResults"), notNullValue());
        assertThat(response.get("totalResults"), equalTo(59));
        assertThat((response.get("results")), is(not(empty())));
    }

    @Test
    void getRequestPositive3ParamsTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("query","pizza")
                .queryParams("cuisine","italian")
                .queryParams("excludeCuisine","greek")
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), containsString("Zucchini Pizza Boats"));
        assertThat(response.get("totalResults"), equalTo(12));
        assertThat(response.get("totalResults"), notNullValue());
        assertThat((response.get("results")), is(not(empty())));
    }

    @Test
    void getRequestAPIKeyErroneous() {
        JsonPath response = given()
                .queryParam("apiKey", "3016bd7643da41f6a68f3f55a1aab")
                .queryParams("excludeCuisine","Mexican")
                .queryParams("diet","vegetarian")
                .queryParams("sort","calories")
                .queryParams("maxCalories","500")
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat(response.get("status"), equalTo("failure"));
        assertThat(response.get("code"), equalTo(401));
        assertThat(response.get("message"), containsString("You are not authorized"));
    }

    @Test
    void getRequestNegativeNumberStringTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParams("query","cake")
                .queryParams("intolerances","Egg,Gluten,Seafood")
                .queryParams("number","gfhfghfj")
                .when()
                .get(url)
                .then()
                .statusCode(404)
                .contentType("text/html;charset=utf-8")
                .statusLine("HTTP/1.1 404 Not Found")
                .body(containsString("The requested resource is not available"));
    }

    @Test
    void getRequestNegativeRecipeBoxIdStringTest() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParams("recipeBoxId","sdgugheriughoij")
                .when()
                .get(url)
                .then()
                .statusCode(404)
                .contentType("text/html;charset=utf-8")
                .body(containsString("Error report"))
                .time(lessThan(3000L));
    }
}

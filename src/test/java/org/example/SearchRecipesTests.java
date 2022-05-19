package org.example;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class SearchRecipesTests {

    private String apiKey = "3016bd7643da41f6a68f3f55a1aabfd0";
    private  String url = "https://api.spoonacular.com/recipes/complexSearch";

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
    }


    @Test
    void getRequestQueryDietNumberMaxReadyTimeTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("query","soup")
                .queryParams("diet","Ovo-Vegetarian")
                .queryParams("number",2)
                .queryParams("maxReadyTime",60)
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), containsString("Soup"));
        assertThat(response.get("totalResults"), notNullValue());
        assertThat(response.get("number"), equalTo(2));
        assertThat((response.get("results")), is(not(empty())));
    }

    @Test
    void getRequestQueryCuisineExcludeCuisineTest() {
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
    void getRequestExcludeCuisineDietSortMaxProteinTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("excludeCuisine","Mexican")
                .queryParams("diet","vegetarian")
                .queryParams("sort","calories")
                .queryParams("maxProtein",20)
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat((response.get("results")), is(not(empty())));
        assertThat(response.get("results[0].nutrition.nutrients[0].name"), containsString("Protein"));
        assertThat(response.get("results[0].nutrition.nutrients[0].amount"), allOf(lessThan(20f)));
    }

    @Test
    void getRequestDietMaxCaloriesTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("diet","Gluten Free")
                .queryParams("maxCalories",400)
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat(response.get("results[0].nutrition.nutrients[0].amount"), allOf(lessThan(400f)));
        assertThat(response.get("totalResults"), notNullValue());
        assertThat((response.get("results")), is(not(empty())));
    }

    @Test
    void getRequestNegativeQueryStringTest() {
        JsonPath response = given()
                .queryParam("apiKey", apiKey)
                .queryParams("query","qwrtesdghdh")
                .expect()
                .statusCode(200)
                .when()
                .get(url)
                .body()
                .jsonPath();
        assertThat((response.get("results")), is((empty())));
        assertThat(response.get("totalResults"), equalTo(0));
    }
}

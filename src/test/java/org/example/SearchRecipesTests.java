package org.example;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class SearchRecipesTests extends AbstractTest{


    private  String url = "https://api.spoonacular.com/recipes/complexSearch";

    @Test
    void getRequestQueryDietNumberTest() {
        SearchRecipesResponse searchRecipesResponse = given()
                .queryParams("query","soup")
                .queryParams("diet","Ovo-Vegetarian")
                .queryParams("number",2)
                .when()
                .get(url)
                .then()
                .extract()
                .response()
                .body()
                .as(SearchRecipesResponse.class);
        assertThat(searchRecipesResponse.getResults().get(0).getTitle(), containsString("Soup"));
        assertThat(searchRecipesResponse.getTotalResults(), notNullValue());
        assertThat(searchRecipesResponse.getNumber(), equalTo(2));
        assertThat(searchRecipesResponse.getResults(),is(not(empty())));

    }

    @Test
    void getRequestQueryCuisineExcludeCuisineTest() {
        SearchRecipesResponse searchRecipesResponse = given()
                .queryParams("query","pizza")
                .queryParams("cuisine","italian")
                .queryParams("excludeCuisine","greek")
                .when()
                .get(url)
                .then()
                .extract()
                .response()
                .body()
                .as(SearchRecipesResponse.class);
        assertThat(searchRecipesResponse.getResults().get(0).getTitle(), containsString("Zucchini Pizza Boats"));
        assertThat(searchRecipesResponse.getTotalResults(), equalTo(12));
        assertThat(searchRecipesResponse.getTotalResults(), notNullValue());
        assertThat(searchRecipesResponse.getResults(), is(not(empty())));
    }

    @Test
    void getRequestExcludeCuisineDietSortMaxProteinTest() {
        SearchRecipesResponse searchRecipesResponse = given()
                .queryParams("excludeCuisine","Mexican")
                .queryParams("diet","vegetarian")
                .queryParams("sort","calories")
                .queryParams("maxProtein",20)
                .when()
                .get(url)
                .then()
                .extract()
                .response()
                .body()
                .as(SearchRecipesResponse.class);
        assertThat(searchRecipesResponse.getResults(), is(not(empty())));
        searchRecipesResponse.getResults().forEach(result ->
                assertThat(result.getNutrition().getNutrients().get(0).getName(), containsString("Protein")));
        assertThat(searchRecipesResponse.getResults().get(0).getNutrition().getNutrients().get(0).getAmount(), allOf(lessThan(20.0)));
    }

    @Test
    void getRequestDietMaxCaloriesTest() {
        SearchRecipesResponse searchRecipesResponse = given()
                .queryParams("diet","Gluten Free")
                .queryParams("maxCalories",400)
                .when()
                .get(url)
                .then()
                .extract()
                .response()
                .body()
                .as(SearchRecipesResponse.class);
        assertThat(searchRecipesResponse.getResults().get(0).getNutrition().getNutrients().get(0).getAmount(), allOf(lessThan(400.0)));
        assertThat(searchRecipesResponse.getTotalResults(), notNullValue());
        assertThat(searchRecipesResponse.getResults(), is(not(empty())));
    }

    @Test
    void getRequestNegativeQueryStringTest() {
        SearchRecipesResponse searchRecipesResponse = given()
                .queryParams("query","qwrtesdghdh")
                .when()
                .get(url)
                .then()
                .extract()
                .response()
                .body()
                .as(SearchRecipesResponse.class);
        assertThat(searchRecipesResponse.getResults(), is((empty())));
        assertThat(searchRecipesResponse.getTotalResults(), equalTo(0));
    }
}




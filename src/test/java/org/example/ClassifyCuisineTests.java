package org.example;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClassifyCuisineTests extends AbstractTest {

    private String url = "https://api.spoonacular.com/recipes/cuisine";


    @Test
    void postRequestPositiveTest3Param() {
        ClassifyCuisineResponse classifyCuisineRespons = given()
                .when()
                .formParam("title","sushi")
                .formParam("ingredientList","rice")
                .formParam("language","en")
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineRespons.getCuisine(), containsString("Japanese"));
        assertThat(classifyCuisineRespons.getConfidence(), equalTo (0.85));
    }

    @Test
    void getRequestPositiveTest2Param() {
        ClassifyCuisineResponse classifyCuisineRespons = given()
                .when()
                .formParam("title","Bruschetta Style Pork & Pasta")
                .formParam("ingredientList","Pasta and Rice")
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineRespons.getCuisine(), containsString("Mediterranean"));
        assertThat(classifyCuisineRespons.getConfidence(), equalTo (0.95));
    }

    @Test
    void getRequestPositiveTest1Param() {
        ClassifyCuisineResponse classifyCuisineRespons = given()
                .when()
                .formParam("title","tiramisu")
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineRespons.getCuisine(), containsString("Mediterranean"));
        assertThat(classifyCuisineRespons.getConfidence(), equalTo (0.85));
    }

    @Test
    void getRequestIngredientListSymbol() {
        ClassifyCuisineResponse classifyCuisineRespons = given()
                .when()
                .formParam("title","Cake Balls")
                .formParam("ingredientList","№;%:")
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineRespons.getConfidence(), equalTo (0.0));
    }

    @Test
    void getRequestTitleSymbol() {
        ClassifyCuisineResponse classifyCuisineRespons = given()
                .when()
                .formParam("title","@##$%^")
                .formParam("ingredientList","cheese")
                .formParam("language","en")
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineRespons.getConfidence(), equalTo (0.0));
    }
}

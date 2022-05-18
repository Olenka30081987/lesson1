package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ShoppingListTests {

    private final String apiKey = "3016bd7643da41f6a68f3f55a1aabfd0";
    private final String url = "https://api.spoonacular.com/mealplanner/olga drevina/shopping-list/items";
    private final String hash = "c3597fc505bea8db3bfa1d49c3e421fe4aa5c8c3";
    private String id;
    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void addShoppingListTest() {
         id = given()
                .queryParam("apiKey", apiKey)
                .queryParam("hash", hash)
                .queryParam("username", "olga drevina")
                .body("{\n"
                        + " \"item\": \"1 kilogram sugar\",\n"
                        + " \"aisle\": \"Baking\",\n"
                        + " \"parse\": true,\n"
                        + "}")
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        given()
                .queryParam("apiKey", apiKey)
                .queryParam("hash", hash)
                .delete(url + "/" + id)
                .then()
                .statusCode(200);
    }
}



package org.example;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ShoppingListTests extends AbstractTest{

    private  String url = "https://api.spoonacular.com/mealplanner/olga drevina/shopping-list/items";
    private  String hash = "c3597fc505bea8db3bfa1d49c3e421fe4aa5c8c3";
    private String id;

    @Test
    void addShoppingListTest() {
        ShoppingListRequest shoppingListRequest = new ShoppingListRequest("1 kilogram sugar", "Baking", true);
        ShoppingListResponse shoppingListResponse = given()
                .queryParam("hash", hash)
                .queryParam("username", "olga drevina")
                .body(shoppingListRequest)
                .when()
                .post(url)
                .then()
                .extract()
                .response()
                .body()
                .as(ShoppingListResponse.class);

        id = shoppingListResponse.getId().toString();
        assertThat(shoppingListResponse.getName(), containsString("sugar"));

        given()
                .queryParam("hash", hash)
                .delete(url + "/" + id)
                .then()
                .statusCode(200);
    }
}



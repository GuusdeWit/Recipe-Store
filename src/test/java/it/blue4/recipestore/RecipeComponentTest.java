package it.blue4.recipestore;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import it.blue4.recipestore.infrastructure.repository.MongoRecipe;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RecipeComponentTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.dropCollection(MongoRecipe.class);
    }

    @Test
    @DisplayName("post to the recipes endpoint should return a 201 and create an entry in the database")
    void postShouldReturn201AndCreateDatabaseEntry() {
        // Given
        var before = mongoTemplate.findAll(MongoRecipe.class);
        assertThat(before).isEmpty();

        given()
                .basePath("/recipes")
                .contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "title": "title",
                                    "description": "description",
                                    "instructions": "instructions for the recipe",
                                    "numberOfServings": 2,
                                    "ingredients": [
                                        {
                                            "name": "ingredient name",
                                            "type": "MEAT",
                                            "amount": 3.5,
                                            "unit": "GRAM"
                                        }
                                    ]
                                }
                                """
                )
                .when()
                .request("POST")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        var after = mongoTemplate.findAll(MongoRecipe.class);
        assertThat(after).hasSize(1);
        var recipe = after.get(0);
        assertThat(recipe.getTitle()).isEqualTo("title");
        assertThat(recipe.getDescription()).isEqualTo("description");
        assertThat(recipe.getInstructions()).isEqualTo("instructions for the recipe");
    }

    @Test
    @DisplayName("post to the recipes endpoint with incomplete data should return a 400 and not create a database entry")
    void postShouldReturn400WithBadData() {
        given()
                .basePath("/recipes")
                .contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "title": "title"
                                }
                                """
                )
                .when()
                .request("POST")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Validation error when processing request"));

        assertThat(mongoTemplate.findAll(MongoRecipe.class)).isEmpty();
    }
}

package it.blue4.recipestore;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import it.blue4.recipestore.infrastructure.repository.MongoIngredient;
import it.blue4.recipestore.infrastructure.repository.MongoRecipe;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

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

    @Nested
    class PostTests {
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

    @Nested
    class GetTests {

        @Test
        @DisplayName("get should return data with 200 if found")
        void getShouldReturn200WhenFound() {
            UUID id = UUID.randomUUID();
            MongoRecipe mongoRecipe = new MongoRecipe(
                    id,
                    "my title",
                    "descriptive",
                    "these are the instructions",
                    5,
                    List.of(
                            new MongoIngredient("name", "MEAT", BigDecimal.ONE, "PIECE")
                    ),
                    false
            );
            mongoTemplate.insert(mongoRecipe);

            given()
                    .basePath("/recipes/" + id)
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id.toString()))
                    .and().body("title", equalTo("my title"))
                    .and().body("description", equalTo("descriptive"))
                    .and().body("instructions", equalTo("these are the instructions"))
                    .and().body("numberOfServings", equalTo(5))
                    .and().body("ingredients[0].name", equalTo("name"))
                    .and().body("ingredients[0].type", equalTo("MEAT"))
                    .and().body("ingredients[0].amount", equalTo(1))
                    .and().body("ingredients[0].unit", equalTo("PIECE"));
        }

        @Test
        @DisplayName("get should return 404 when not found")
        void getShouldReturn404WhenNotFound() {
            UUID randomId = UUID.randomUUID();
            given()
                    .basePath("/recipes/" + randomId)
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("message", containsString("The requested data was not found"));
        }

        @Test
        @DisplayName("get all should return all persisted recipes")
        void getAllShouldReturnAll() {

            UUID id1 = UUID.randomUUID();
            MongoRecipe mongoRecipe1 = new MongoRecipe(
                    id1,
                    "my title",
                    "descriptive",
                    "these are the instructions",
                    5,
                    List.of(
                            new MongoIngredient("ing", "MEAT", BigDecimal.ONE, "PIECE")
                    ),
                    false
            );

            UUID id2 = UUID.randomUUID();
            MongoRecipe mongoRecipe2 = new MongoRecipe(
                    id2,
                    "my second title",
                    "descriptive",
                    "these are the instructions",
                    1,
                    List.of(
                            new MongoIngredient("name", "MEAT", BigDecimal.valueOf(1.7), "TEASPOON")
                    ),
                    false
            );

            mongoTemplate.insert(mongoRecipe1);
            mongoTemplate.insert(mongoRecipe2);

            given()
                    .basePath("/recipes")
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("size()", equalTo(2));
        }
    }
}

package it.blue4.recipestore;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import it.blue4.recipestore.infrastructure.repository.MongoTestPersistService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(MongoTestPersistService.class)
class RecipeComponentTest {

    @Autowired
    TestPersistService persistService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        persistService.removeAllData();
    }

    @Nested
    class PostTests {
        @Test
        @DisplayName("post to the recipes endpoint should return a 201 and create an entry in the database")
        void postShouldReturn201AndCreateDatabaseEntry() {
            // Given
            assertThat(persistService.dataStoreIsEmpty()).isTrue();

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

            assertThat(persistService.dataStoreContainsExactly(1)).isTrue();
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

            assertThat(persistService.dataStoreIsEmpty()).isTrue();
        }
    }

    @Nested
    class GetTests {

        @Test
        @DisplayName("get should return data with 200 if found")
        void getShouldReturn200WhenFound() {
            UUID id = UUID.randomUUID();
            persistService.persistOneWithId(id);

            given()
                    .basePath("/recipes/" + id)
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id.toString()))
                    .and().body("title", equalTo(TestPersistService.title))
                    .and().body("description", equalTo(TestPersistService.description))
                    .and().body("instructions", equalTo(TestPersistService.instructions))
                    .and().body("numberOfServings", equalTo(TestPersistService.numberOfServings))
                    .and().body("isVegetarian", equalTo(TestPersistService.vegetarian))
                    .and().body("ingredients[0].name", equalTo(TestPersistService.ingredientName))
                    .and().body("ingredients[0].type", equalTo(TestPersistService.ingredientType))
                    .and().body("ingredients[0].amount", is(TestPersistService.ingredientAmount.floatValue()))
                    .and().body("ingredients[0].unit", equalTo(TestPersistService.ingredientUnit));
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
            UUID id2 = UUID.randomUUID();
            persistService.persistOneWithId(id1);
            persistService.persistOneWithId(id2);

            given()
                    .basePath("/recipes")
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("size()", equalTo(2));
        }

        @Test
        @DisplayName("get all should apply filters")
        void getAllShouldReturnFiltered() {
            persistService.persistMultiple(Stream.generate(UUID::randomUUID).limit(3).toList());

            given()
                    .basePath("/recipes")
                    .queryParam("isVegetarian", false)
                    .queryParam("numberOfServings", 3)
                    .queryParam("instructionContains", "instructions")
                    .queryParam("includeIngredients", "sausage")
                    .queryParam("excludeIngredients", "fish")
                    .when()
                    .request("GET")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("size()", equalTo(0));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @DisplayName("delete should remove persisted data and return 200")
        void deleteShouldRemoveFromDataStore() {
            UUID id = UUID.randomUUID();
            persistService.persistOneWithId(id);

            given()
                    .basePath("/recipes/" + id)
                    .when()
                    .request("DELETE")
                    .then()
                    .statusCode(HttpStatus.SC_OK);

            assertThat(persistService.dataStoreDoesNotContainElementWithId(id)).isFalse();
        }
    }
}

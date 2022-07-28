package it.blue4.recipestore.infrastructure.repository;

import it.blue4.recipestore.TestPersistService;
import it.blue4.recipestore.domain.model.Description;
import it.blue4.recipestore.domain.model.Instructions;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.model.Servings;
import it.blue4.recipestore.domain.model.Title;
import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;
import it.blue4.recipestore.domain.request.filter.Filter;
import it.blue4.recipestore.domain.request.filter.IngredientsFilter;
import it.blue4.recipestore.domain.request.filter.InstructionsContainsFilter;
import it.blue4.recipestore.domain.request.filter.ServingsFilter;
import it.blue4.recipestore.domain.request.filter.VegetarianFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Import(MongoTestPersistService.class)
class MongoRecipeRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoRecipeRepository repository;

    @Autowired
    MongoTestPersistService persistService;

    @AfterEach
    void reset() {
        mongoTemplate.dropCollection(MongoRecipe.class);
    }

    private final Recipe recipe = new Recipe(
            new Title("title"),
            new Description("description"),
            new Instructions("instructions"),
            new Servings(2),
            List.of(
                    new Ingredient(
                            new IngredientName("first"),
                            IngredientType.PLANT_BASED,
                            new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)
                    ),
                    new Ingredient(
                            new IngredientName("second"),
                            IngredientType.SPICE,
                            new IngredientQuantity(BigDecimal.TEN, MeasuringUnit.TABLESPOON)
                    )
            )
    );

    @Nested
    class PersistTests {
        @Test
        void persistShouldStoreDataCorrectly() {
            // When
            repository.persist(recipe);

            // Then
            var result = mongoTemplate.findById(recipe.getRecipeId().id(), MongoRecipe.class, "recipe");
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(recipe.getTitle().title());
            assertThat(result.getDescription()).isEqualTo(recipe.getDescription().description());
            assertThat(result.getInstructions()).isEqualTo(recipe.getInstructions().instructions());
            assertThat(result.getNumberOfServings()).isEqualTo(recipe.getServings().number());
            assertThat(result.getIngredients()).hasSize(2);

            var firstIngredient = result.getIngredients().get(0);
            assertThat(firstIngredient.name()).isEqualTo(recipe.getIngredients().get(0).name().name());
            assertThat(firstIngredient.type()).isEqualTo(recipe.getIngredients().get(0).type().name());
            assertThat(firstIngredient.amount()).isEqualTo(recipe.getIngredients().get(0).quantity().amount());
            assertThat(firstIngredient.unit()).isEqualTo(recipe.getIngredients().get(0).quantity().unit().name());

            var secondIngredient = result.getIngredients().get(1);
            assertThat(secondIngredient.name()).isEqualTo(recipe.getIngredients().get(1).name().name());
            assertThat(secondIngredient.type()).isEqualTo(recipe.getIngredients().get(1).type().name());
            assertThat(secondIngredient.amount()).isEqualTo(recipe.getIngredients().get(1).quantity().amount());
            assertThat(secondIngredient.unit()).isEqualTo(recipe.getIngredients().get(1).quantity().unit().name());
        }

        @ParameterizedTest
        @MethodSource("provideIngredientTypes")
        void persistShouldStoreVegetarianValueBasedOnRecipe(IngredientType type, boolean expected) {
            // Given
            Recipe recipe = new Recipe(
                    new Title("title"),
                    new Description("description"),
                    new Instructions("instructions"),
                    new Servings(2),
                    List.of(
                            new Ingredient(
                                    new IngredientName("first"),
                                    type,
                                    new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)
                            )
                    )
            );

            // When
            repository.persist(recipe);

            // Then
            var result = mongoTemplate.findById(recipe.getRecipeId().id(), MongoRecipe.class);
            assert result != null;
            assertThat(result.isVegetarian()).isEqualTo(expected);
        }

        static Stream<Arguments> provideIngredientTypes() {
            return Stream.of(
                    Arguments.of(IngredientType.PLANT_BASED, true),
                    Arguments.of(IngredientType.MEAT, false)
            );
        }
    }

    @Nested
    class RetrieveTests {
        @Test
        void retrieveByIdShouldReturnEmptyIfNotFound() {
            // Given
            UUID randomId = UUID.randomUUID();

            // When
            var result = repository.retrieveById(new RecipeId(randomId));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void retrieveByIdShouldReturnRecipeWhenFound() {
            // Given
            UUID id = UUID.randomUUID();
            persistService.persistOneWithId(id);

            // When
            var result = repository.retrieveById(new RecipeId(id));

            // Then
            assertThat(result).isNotEmpty();
            Recipe recipe = result.get();
            assertThat(recipe.getRecipeId()).isEqualTo(new RecipeId(id));
            assertThat(recipe.getTitle().title()).isEqualTo(TestPersistService.title);
            assertThat(recipe.getDescription().description()).isEqualTo(TestPersistService.description);
            assertThat(recipe.getInstructions().instructions()).isEqualTo(TestPersistService.instructions);
            assertThat(recipe.getServings().number()).isEqualTo(TestPersistService.numberOfServings);

            assertThat(recipe.getIngredients()).hasSize(1);
            Ingredient ingredient = recipe.getIngredients().get(0);
            assertThat(ingredient.name().name()).isEqualTo(TestPersistService.ingredientName);
            assertThat(ingredient.type().name()).isEqualTo(TestPersistService.ingredientType);
            assertThat(ingredient.quantity().amount()).isEqualTo(TestPersistService.ingredientAmount);
            assertThat(ingredient.quantity().unit().name()).isEqualTo(TestPersistService.ingredientUnit);
        }

        @Test
        void retrieveAllShouldReturnListOfAllPersistedRecipes() {
            // Given
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();
            persistService.persistOneWithId(id1);
            persistService.persistOneWithId(id2);

            // When
            var result = repository.retrieveAll();

            // Then
            assertThat(result).hasSize(2);
        }

        @ParameterizedTest
        @MethodSource("provideFilters")
        void retrieveAllFilteredShouldExecuteFilters(List<Filter> filters, List<UUID> allIds, List<UUID> expectedIds) {
            // Given
            persistService.persistMultiple(allIds);

            // When
            var result = repository.retrieveAllFilteredBy(filters);

            // Then
            assertThat(result.stream().map(Recipe::getRecipeId).map(RecipeId::id)).containsExactlyElementsOf(expectedIds);
        }

        static Stream<Arguments> provideFilters() {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();
            UUID id3 = UUID.randomUUID();
            List<UUID> allIds = List.of(id1, id2, id3);
            return Stream.of(
                    Arguments.of(
                            List.of(new VegetarianFilter(true)),
                            allIds,
                            List.of(id2)
                    ),
                    Arguments.of(
                            List.of(new ServingsFilter(2)),
                            allIds,
                            List.of(id1)
                    ),
                    Arguments.of(
                            List.of(new InstructionsContainsFilter("hard")),
                            allIds,
                            List.of(id3)
                    ),
                    Arguments.of(
                            List.of(new IngredientsFilter(List.of("cheese", "fish"), Collections.emptyList())),
                            allIds,
                            List.of(id3)
                    ),
                    Arguments.of(
                            List.of(new IngredientsFilter(Collections.emptyList(), List.of("cheese", "fish"))),
                            allIds,
                            List.of(id1)
                    ),
                    Arguments.of(
                            List.of(new IngredientsFilter(Collections.emptyList(), List.of("fish")), new ServingsFilter(3)),
                            allIds,
                            List.of(id2)
                    ),
                    Arguments.of(
                            List.of(new InstructionsContainsFilter("instructions"), new VegetarianFilter(false)),
                            allIds,
                            List.of(id1, id3)
                    ),
                    Arguments.of(
                            List.of(
                                    new InstructionsContainsFilter("instructions"),
                                    new VegetarianFilter(false),
                                    new ServingsFilter(3),
                                    new IngredientsFilter(List.of("sausage"), List.of("fish"))
                            ),
                            allIds,
                            Collections.emptyList()
                    ),
                    Arguments.of(
                            List.of(
                                    new IngredientsFilter(List.of("cheese"), List.of("fish"))
                            ),
                            allIds,
                            List.of(id2)
                    ),
                    Arguments.of(
                            List.of(),
                            allIds,
                            allIds
                    )
            );
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void deleteShouldDeletePersisted() {
            // Given
            UUID id = UUID.randomUUID();
            persistService.persistOneWithId(id);

            // When
            repository.delete(new RecipeId(id));

            // Then
            var result = mongoTemplate.findById(id, MongoRecipe.class);
            assertThat(result).isNull();
        }

        @Test
        void deleteShouldNotThrowWhenIdDoesNotExist() {
            // Given
            UUID id = UUID.randomUUID();

            // Then
            assertDoesNotThrow(() -> repository.delete(new RecipeId(id)));
        }
    }
}

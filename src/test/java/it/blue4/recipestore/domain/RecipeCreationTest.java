package it.blue4.recipestore.domain;

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
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.IncomingIngredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeCreationTest {

    private final CreateRecipeRequest defaultRequest = new CreateRecipeRequest(
            "title",
            "description",
            "instructions for the recipe",
            2,
            List.of(
                    new IncomingIngredient("sausage", "MEAT", BigDecimal.valueOf(1), "PIECE")
            )
    );

    private final RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private final RecipeService recipeService = new RecipeService(repository);

    @Nested
    class CreateShouldPersistCorrectData {

        @Test
        @DisplayName("create should persist title from the request")
        void createShouldPersistTitle() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getTitle()).isInstanceOf(Title.class);
            assertThat(captured.getTitle().title()).isEqualTo("title");
        }

        @Test
        @DisplayName("create should persist description from the request")
        void createShouldPersistDescription() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getDescription()).isInstanceOf(Description.class);
            assertThat(captured.getDescription().description()).isEqualTo("description");
        }

        @Test
        @DisplayName("create should persist instructions from the request")
        void createShouldPersistInstruction() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getInstructions()).isInstanceOf(Instructions.class);
            assertThat(captured.getInstructions().instructions()).isEqualTo("instructions for the recipe");
        }

        @Test
        @DisplayName("create should persist number of servings from the request")
        void createShouldPersistId() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getServings()).isInstanceOf(Servings.class);
            assertThat(captured.getServings().number()).isEqualTo(2);
        }

        @Test
        @DisplayName("create should persist a RecipeId")
        void createShouldPersistNumberOfServings() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getRecipeId()).isInstanceOf(RecipeId.class);
            assertThat(captured.getRecipeId().id()).isInstanceOf(UUID.class);
        }

        @Test
        @DisplayName("create should persist ingredients from the request")
        void createShouldPersistIngredients() {
            // When
            recipeService.create(defaultRequest);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getIngredients()).hasSize(1);
            Ingredient ingredient = captured.getIngredients().get(0);
            assertThat(ingredient.name()).isInstanceOf(IngredientName.class);
            assertThat(ingredient.name().name()).isEqualTo("sausage");
            assertThat(ingredient.type()).isInstanceOf(IngredientType.class);
            assertThat(ingredient.type()).isEqualTo(IngredientType.MEAT);
            assertThat(ingredient.quantity()).isInstanceOf(IngredientQuantity.class);
            assertThat(ingredient.quantity().amount()).isEqualTo(BigDecimal.valueOf(1));
            assertThat(ingredient.quantity().unit()).isInstanceOf(MeasuringUnit.class);
            assertThat(ingredient.quantity().unit()).isEqualTo(MeasuringUnit.PIECE);
        }
    }

    @Nested
    class CreationOfBusinessEnums {
        @ParameterizedTest
        @MethodSource("provideAllowedIngredientTypes")
        void providedIngredientTypesShouldBePersisted(String input, IngredientType expected) {
            // Given
            CreateRecipeRequest request = new CreateRecipeRequest(
                    "title",
                    "description",
                    "instructions",
                    2,
                    List.of(new IncomingIngredient("name", input, BigDecimal.ONE, "PIECE"))
            );

            // When
            recipeService.create(request);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getIngredients().get(0).type()).isEqualTo(expected);
        }

        static Stream<Arguments> provideAllowedIngredientTypes() {
            return Stream.of(
                    Arguments.of("MEAT", IngredientType.MEAT),
                    Arguments.of("FISH", IngredientType.FISH),
                    Arguments.of("DAIRY", IngredientType.DAIRY),
                    Arguments.of("PLANT_BASED", IngredientType.PLANT_BASED),
                    Arguments.of("SPICE", IngredientType.SPICE)
            );
        }

        @ParameterizedTest
        @MethodSource("provideAllowedMeasuringUnits")
        void providedMeasuringUnitsShouldBePersisted(String input, MeasuringUnit expected) {
            // Given
            CreateRecipeRequest request = new CreateRecipeRequest(
                    "title",
                    "description",
                    "instructions",
                    2,
                    List.of(new IncomingIngredient("name", "MEAT", BigDecimal.ONE, input))
            );

            // When
            recipeService.create(request);

            // Then
            Recipe captured = validatePersistIsTriggeredAndReturnArgument();

            assertThat(captured.getIngredients().get(0).quantity().unit()).isEqualTo(expected);
        }

        static Stream<Arguments> provideAllowedMeasuringUnits() {
            return Stream.of(
                    Arguments.of("CM", MeasuringUnit.CM),
                    Arguments.of("GRAM", MeasuringUnit.GRAM),
                    Arguments.of("ML", MeasuringUnit.ML),
                    Arguments.of("TEASPOON", MeasuringUnit.TEASPOON),
                    Arguments.of("TABLESPOON", MeasuringUnit.TABLESPOON),
                    Arguments.of("PIECE", MeasuringUnit.PIECE)
            );
        }
    }

    private Recipe validatePersistIsTriggeredAndReturnArgument() {
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        Mockito.verify(repository).persist(recipeCaptor.capture());
        return recipeCaptor.getValue();
    }
}

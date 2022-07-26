package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeTest {

    @Test
    @DisplayName("getIngredients should provide an unmodifiable list")
    void getIngredientsShouldBeUnmodifiable() {
        // Given
        Recipe recipe = new Recipe(
                new Title("title"),
                new Description("description"),
                new Instructions("instructions"),
                new Servings(2),
                new ArrayList<>()
        );

        // When
        List<Ingredient> ingredients = recipe.getIngredients();

        // Then
        assertThrows(UnsupportedOperationException.class, () -> ingredients.add(null));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void constructor_shouldNotAllowNullParameters(
            Title title,
            Description description,
            Instructions instructions,
            Servings servings,
            List<Ingredient> ingredients
    ) {
        assertThrows(ValidationException.class, () -> new Recipe(
                title,
                description,
                instructions,
                servings,
                ingredients)
        );
    }

    static Stream<Arguments> provideInvalidArguments() {
        Title title = new Title("title");
        Description description = new Description("Description");
        Instructions instructions = new Instructions("Instructions");
        Servings servings = new Servings(2);
        Ingredient ingredient = new Ingredient(
                new IngredientName("ingredient name"),
                IngredientType.MEAT,
                new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)
        );
        return Stream.of(
                Arguments.of(
                        null,
                        description,
                        instructions,
                        servings,
                        emptyList()
                ),
                Arguments.of(
                        title,
                        null,
                        instructions,
                        servings,
                        emptyList()
                ),
                Arguments.of(
                        title,
                        description,
                        null,
                        servings,
                        emptyList()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        null,
                        emptyList()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        null
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        new ArrayList<>(Arrays.asList(null, ingredient))
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        new ArrayList<>(Arrays.asList(ingredient, null))
                )
        );
    }
}
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
import static org.assertj.core.api.Assertions.assertThat;
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
    @MethodSource("provideIngredientTypes")
    void isVegetarianBasedOnMeatOrFish(List<IngredientType> types, boolean expected) {
        // Given
        Recipe recipe = new Recipe(
                new Title("title"),
                new Description("description"),
                new Instructions("instructions"),
                new Servings(2),
                types.stream().map(type -> new Ingredient(
                        new IngredientName("tomato"),
                        type,
                        new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE))).toList()
        );

        // When
        boolean result = recipe.isVegetarian();

        // Then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> provideIngredientTypes() {
        return Stream.of(
                Arguments.of(
                        List.of(IngredientType.PLANT_BASED),
                        true
                ),
                Arguments.of(
                        List.of(IngredientType.PLANT_BASED, IngredientType.DAIRY, IngredientType.SPICE),
                        true
                ),
                Arguments.of(
                        List.of(IngredientType.MEAT),
                        false
                ),
                Arguments.of(
                        List.of(IngredientType.FISH),
                        false
                ),
                Arguments.of(
                        List.of(IngredientType.PLANT_BASED, IngredientType.DAIRY, IngredientType.MEAT),
                        false
                ),
                Arguments.of(
                        List.of(),
                        true
                )
        );
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

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentsForAllArgsConstructor")
    void constructor_shouldNotAllowNullParametersForAllArgs(
            Title title,
            Description description,
            Instructions instructions,
            Servings servings,
            List<Ingredient> ingredients,
            RecipeId recipeId
    ) {
        assertThrows(ValidationException.class, () -> new Recipe(
                title,
                description,
                instructions,
                servings,
                ingredients,
                recipeId)
        );
    }

    static Stream<Arguments> provideInvalidArgumentsForAllArgsConstructor() {
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
                        title,
                        description,
                        instructions,
                        servings,
                        emptyList(),
                        null
                ),
                Arguments.of(
                        null,
                        description,
                        instructions,
                        servings,
                        emptyList(),
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        null,
                        instructions,
                        servings,
                        emptyList(),
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        description,
                        null,
                        servings,
                        emptyList(),
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        null,
                        emptyList(),
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        null,
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        new ArrayList<>(Arrays.asList(null, ingredient)),
                        new RecipeId()
                ),
                Arguments.of(
                        title,
                        description,
                        instructions,
                        servings,
                        new ArrayList<>(Arrays.asList(ingredient, null)),
                        new RecipeId()
                )
        );
    }
}
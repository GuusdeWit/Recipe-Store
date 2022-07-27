package it.blue4.recipestore.infrastructure;

import it.blue4.recipestore.domain.model.Description;
import it.blue4.recipestore.domain.model.Instructions;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.Servings;
import it.blue4.recipestore.domain.model.Title;
import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MongoRecipeRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoRecipeRepository repository;

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
        assertThat(firstIngredient.getName()).isEqualTo(recipe.getIngredients().get(0).name().name());
        assertThat(firstIngredient.getType()).isEqualTo(recipe.getIngredients().get(0).type().name());
        assertThat(firstIngredient.getAmount()).isEqualTo(recipe.getIngredients().get(0).quantity().amount());
        assertThat(firstIngredient.getUnit()).isEqualTo(recipe.getIngredients().get(0).quantity().unit().name());

        var secondIngredient = result.getIngredients().get(1);
        assertThat(secondIngredient.getName()).isEqualTo(recipe.getIngredients().get(1).name().name());
        assertThat(secondIngredient.getType()).isEqualTo(recipe.getIngredients().get(1).type().name());
        assertThat(secondIngredient.getAmount()).isEqualTo(recipe.getIngredients().get(1).quantity().amount());
        assertThat(secondIngredient.getUnit()).isEqualTo(recipe.getIngredients().get(1).quantity().unit().name());
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

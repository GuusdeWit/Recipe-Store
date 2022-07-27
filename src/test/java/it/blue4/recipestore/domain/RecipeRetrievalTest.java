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
import it.blue4.recipestore.domain.request.RetrieveRecipeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class RecipeRetrievalTest {

    private final RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private final RecipeService recipeService = new RecipeService(repository);

    private final Recipe defaultRecipe = new Recipe(
            new Title("title"),
            new Description("description"),
            new Instructions("instructions"),
            new Servings(2),
            List.of(
                    new Ingredient(
                            new IngredientName("first"),
                            IngredientType.PLANT_BASED,
                            new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)
                    )
            )
    );

    @Test
    @DisplayName("retrieve should return a Recipe from the repository")
    void retrieveShouldReturnRecipe() {
        // Given
        UUID id = defaultRecipe.getRecipeId().id();
        RetrieveRecipeRequest request = new RetrieveRecipeRequest(id);

        // When
        Mockito.when(repository.retrieveById(new RecipeId(id))).thenReturn(Optional.of(defaultRecipe));
        Recipe result = recipeService.retrieve(request);

        // Then
        assertThat(result).isEqualTo(defaultRecipe);
    }

    @Test
    @DisplayName("retrieve should throw a NotFoundException if repository returns empty optional")
    void retrieveShouldThrowIfNotFound() {
        // Given
        UUID id = defaultRecipe.getRecipeId().id();
        RetrieveRecipeRequest request = new RetrieveRecipeRequest(id);

        // When
        Mockito.when(repository.retrieveById(any())).thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class, () -> recipeService.retrieve(request));
    }
}

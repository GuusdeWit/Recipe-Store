package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.request.DeleteRecipeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class RecipeDeletionTest {

    private final RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private final RecipeService recipeService = new RecipeService(repository);

    @Test
    @DisplayName("delete should trigger deletion of a recipe with the requested id")
    void deleteShouldTriggerDelete() {
        // Given
        UUID id = UUID.randomUUID();
        DeleteRecipeRequest request = new DeleteRecipeRequest(id);

        // When
        recipeService.delete(request);

        // Then
        verify(repository).delete(new RecipeId(id));
    }

    @Test
    @DisplayName("delete should throw ValidationException when id is not provided")
    void deleteShouldThrowWhenNullId() {
        // Given
        DeleteRecipeRequest request = new DeleteRecipeRequest(null);

        // When
        assertThrows(ValidationException.class, () -> recipeService.delete(request));
    }
}

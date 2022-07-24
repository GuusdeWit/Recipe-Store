package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.Recipe;
import it.blue4.recipestore.domain.RecipeRepository;
import it.blue4.recipestore.domain.Title;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class RecipeCreationTest {

    @Test
    @DisplayName("create should persist title from the request")
    void  createShouldPersistTitle(){
        // Given
        CreateRecipeRequest request = new CreateRecipeRequest("title");

        // When
        RecipeRepository repository = Mockito.mock(RecipeRepository.class);
        RecipeService recipeService = new RecipeService(repository);

        recipeService.create(request);

        // Then
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        Mockito.verify(repository).persist(recipeCaptor.capture());
        Recipe captured = recipeCaptor.getValue();

        Assertions.assertThat(captured.getTitle()).isInstanceOf(Title.class);
        Assertions.assertThat(captured.getTitle().title()).isEqualTo("title");
    }
}

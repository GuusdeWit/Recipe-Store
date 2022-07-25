package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class RecipeCreationTest {

    private final CreateRecipeRequest defaultRequest = new CreateRecipeRequest(
            "title",
            "description",
            "instructions for the recipe",
            2
    );

    private final RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private final RecipeService recipeService = new RecipeService(repository);


    @Test
    @DisplayName("create should persist title from the request")
    void createShouldPersistTitle() {
        // When
        recipeService.create(defaultRequest);

        // Then
        Recipe captured = validatePersistIsTriggeredAndReturnArgument();

        Assertions.assertThat(captured.getTitle()).isInstanceOf(Title.class);
        Assertions.assertThat(captured.getTitle().title()).isEqualTo("title");
    }

    @Test
    @DisplayName("create should persist description from the request")
    void createShouldPersistDescription() {
        // When
        recipeService.create(defaultRequest);

        // Then
        Recipe captured = validatePersistIsTriggeredAndReturnArgument();

        Assertions.assertThat(captured.getDescription()).isInstanceOf(Description.class);
        Assertions.assertThat(captured.getDescription().description()).isEqualTo("description");
    }

    @Test
    @DisplayName("create should persist instructions from the request")
    void createShouldPersistInstruction() {
        // When
        recipeService.create(defaultRequest);

        // Then
        Recipe captured = validatePersistIsTriggeredAndReturnArgument();

        Assertions.assertThat(captured.getInstructions()).isInstanceOf(Instructions.class);
        Assertions.assertThat(captured.getInstructions().instructions()).isEqualTo("instructions for the recipe");
    }

    @Test
    @DisplayName("create should persist number of servings from the request")
    void createShouldPersistNumberOfServings() {
        // When
        recipeService.create(defaultRequest);

        // Then
        Recipe captured = validatePersistIsTriggeredAndReturnArgument();

        Assertions.assertThat(captured.getServings()).isInstanceOf(Servings.class);
        Assertions.assertThat(captured.getServings().number()).isEqualTo(2);
    }

    private Recipe validatePersistIsTriggeredAndReturnArgument() {
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        Mockito.verify(repository).persist(recipeCaptor.capture());
        return recipeCaptor.getValue();
    }
}

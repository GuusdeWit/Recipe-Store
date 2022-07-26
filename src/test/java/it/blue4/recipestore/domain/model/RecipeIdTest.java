package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeIdTest {
    @Test
    @DisplayName("constructing RecipeId with id null should throw a ValidationException")
    void nameShouldNotBeNull() {
        assertThrows(ValidationException.class, () -> new RecipeId(null));
    }
}
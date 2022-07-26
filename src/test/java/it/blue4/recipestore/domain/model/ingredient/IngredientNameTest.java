package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientNameTest {
    @Test
    @DisplayName("constructing IngredientName with name null should throw a ValidationException")
    void nameShouldNotBeNull() {
        assertThrows(ValidationException.class, () -> new IngredientName(null));
    }
}
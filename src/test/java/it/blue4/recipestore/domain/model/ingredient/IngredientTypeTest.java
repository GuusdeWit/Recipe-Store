package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientTypeTest {

    @Test
    @DisplayName("parsing with an unknown type should throw a ValidationException")
    void unknownIngredientType() {
        assertThrows(ValidationException.class, () -> IngredientType.parseValue("UNKNOWN"));
    }
}
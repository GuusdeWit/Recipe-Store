package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructionsTest {
    @Test
    @DisplayName("constructing Instructions with instructions null should throw a ValidationException")
    void nameShouldNotBeNull() {
        assertThrows(ValidationException.class, () -> new Instructions(null));
    }

}
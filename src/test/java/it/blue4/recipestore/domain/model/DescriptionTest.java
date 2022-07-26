package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DescriptionTest {
    @Test
    @DisplayName("constructing Description with name description should throw a ValidationException")
    void descriptionShouldNotBeNull() {
        assertThrows(ValidationException.class, () -> new Description(null));
    }
}
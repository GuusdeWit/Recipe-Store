package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeasuringUnitTest {

    @Test
    @DisplayName("parsing with an unknown unit should throw a ValidationException")
    void unknownMeasuringUnit() {
        assertThrows(ValidationException.class, () -> MeasuringUnit.parseValue("UNKNOWN"));
    }
}
package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;

public enum MeasuringUnit {
    CM, GRAM, ML, TEASPOON, TABLESPOON, PIECE;

    public static MeasuringUnit parseValue(String input) {
        try {
            return MeasuringUnit.valueOf(input);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException();
        }
    }
}

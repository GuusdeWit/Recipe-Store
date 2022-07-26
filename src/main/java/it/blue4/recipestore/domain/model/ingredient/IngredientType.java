package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;

public enum IngredientType {
    FISH, DAIRY, PLANT_BASED, SPICE, MEAT;

    public static IngredientType parseValue(String input) {
        try {
            return IngredientType.valueOf(input);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException();
        }
    }
}

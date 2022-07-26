package it.blue4.recipestore.domain.model.ingredient;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record Ingredient(IngredientName name, IngredientType type, IngredientQuantity quantity) {
    public Ingredient {
        requireNonNull(name);
        requireNonNull(type);
        requireNonNull(quantity);
    }
}

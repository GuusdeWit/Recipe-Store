package it.blue4.recipestore.domain.model.ingredient;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record IngredientName(String name) {
    public IngredientName {
        requireNonNull(name);
    }
}


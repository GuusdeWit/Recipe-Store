package it.blue4.recipestore.domain.model;

import java.util.UUID;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record RecipeId(UUID id) {
    public RecipeId {
        requireNonNull(id);
    }

    public RecipeId() {
        this(UUID.randomUUID());
    }
}

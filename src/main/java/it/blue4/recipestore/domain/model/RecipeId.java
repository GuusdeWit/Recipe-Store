package it.blue4.recipestore.domain.model;

import java.util.UUID;

public record RecipeId(UUID id) {
    public RecipeId() {
        this(UUID.randomUUID());
    }
}

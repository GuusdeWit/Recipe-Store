package it.blue4.recipestore.domain;

import java.util.UUID;

public record RecipeId(UUID id) {
    public RecipeId() {
        this(UUID.randomUUID());
    }
}

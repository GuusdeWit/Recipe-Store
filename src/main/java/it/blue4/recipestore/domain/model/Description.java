package it.blue4.recipestore.domain.model;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record Description(String description) {
    public Description {
        requireNonNull(description);
    }
}

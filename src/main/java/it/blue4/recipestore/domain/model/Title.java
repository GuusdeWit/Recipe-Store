package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record Title(String title) {
    public Title {
        requireNonNull(title);
        if (title.length() > 50) {
            throw new ValidationException();
        }
    }
}

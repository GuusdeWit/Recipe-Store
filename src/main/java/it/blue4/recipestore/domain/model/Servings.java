package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;

public record Servings(int number) {
    public Servings {
        if (number <= 0) {
            throw new ValidationException();
        }
    }
}

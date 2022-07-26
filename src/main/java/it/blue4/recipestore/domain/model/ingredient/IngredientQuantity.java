package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;

import java.math.BigDecimal;

import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public record IngredientQuantity(BigDecimal amount, MeasuringUnit unit) {
    public IngredientQuantity {
        requireNonNull(amount);
        requireNonNull(unit);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException();
        }
    }
}

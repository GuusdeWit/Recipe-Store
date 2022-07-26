package it.blue4.recipestore.domain.model.ingredient;

import java.math.BigDecimal;

public record IngredientQuantity(BigDecimal amount, MeasuringUnit unit) {
}

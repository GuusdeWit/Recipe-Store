package it.blue4.recipestore.application.dto;

import it.blue4.recipestore.domain.model.ingredient.Ingredient;

import java.math.BigDecimal;

public record IngredientDTO(String name, String type, BigDecimal amount, String unit) {
    public static IngredientDTO from(Ingredient ingredient) {
        return new IngredientDTO(
                ingredient.name().name(),
                ingredient.type().name(),
                ingredient.quantity().amount(),
                ingredient.quantity().unit().name()
        );
    }
}

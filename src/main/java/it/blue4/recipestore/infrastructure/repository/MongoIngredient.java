package it.blue4.recipestore.infrastructure.repository;

import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;

import java.math.BigDecimal;

public record MongoIngredient(String name, String type, BigDecimal amount, String unit) {
    public Ingredient toDomainIngredient() {
        return new Ingredient(
                new IngredientName(name),
                IngredientType.parseValue(type),
                new IngredientQuantity(amount, MeasuringUnit.parseValue(unit))
        );
    }
}

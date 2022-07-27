package it.blue4.recipestore.infrastructure.repository;

import java.math.BigDecimal;

public record MongoIngredient(String name, String type, BigDecimal amount, String unit) {
}

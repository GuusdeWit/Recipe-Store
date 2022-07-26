package it.blue4.recipestore.domain.request;

import java.math.BigDecimal;

public record IncomingIngredient(String name, String type, BigDecimal amount, String unit) {
}

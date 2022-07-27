package it.blue4.recipestore.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.blue4.recipestore.domain.request.IncomingIngredient;

import java.math.BigDecimal;

public class IncomingIngredientDTO {
    private final String name;
    private final String type;
    private final BigDecimal amount;
    private final String unit;

    public IncomingIngredient toIncomingIngredient() {
        return new IncomingIngredient(name, type, amount, unit);
    }

    @JsonCreator
    public IncomingIngredientDTO(String name, String type, BigDecimal amount, String unit) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.unit = unit;
    }
}

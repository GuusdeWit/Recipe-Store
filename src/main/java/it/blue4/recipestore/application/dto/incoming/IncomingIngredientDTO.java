package it.blue4.recipestore.application.dto.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import it.blue4.recipestore.domain.request.IncomingIngredient;

import java.math.BigDecimal;

public class IncomingIngredientDTO {
    @Schema(
            required = true,
            example = "diced carrots"
    )
    private final String name;
    @Schema(
            required = true
    )
    private final Type type;
    @Schema(
            required = true,
            example = "3.5",
            minimum = "0",
            exclusiveMaximum = true
    )
    private final BigDecimal amount;
    @Schema(
            required = true
    )
    private final Unit unit;

    public IncomingIngredient toIncomingIngredient() {
        return new IncomingIngredient(name, type.name(), amount, unit.name());
    }

    @JsonCreator
    public IncomingIngredientDTO(String name, String type, BigDecimal amount, Unit unit) {
        this.name = name;
        this.type = Type.valueOf(type);
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }

    enum Type {
        FISH, DAIRY, PLANT_BASED, SPICE, MEAT
    }

    enum Unit {
        CM, GRAM, ML, TEASPOON, TABLESPOON, PIECE
    }
}

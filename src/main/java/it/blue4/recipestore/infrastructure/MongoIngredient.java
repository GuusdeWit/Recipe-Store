package it.blue4.recipestore.infrastructure;

import java.math.BigDecimal;

public class MongoIngredient {
    private final String name;
    private final String type;
    private final BigDecimal amount;
    private final String unit;

    public MongoIngredient(String name, String type, BigDecimal amount, String unit) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}

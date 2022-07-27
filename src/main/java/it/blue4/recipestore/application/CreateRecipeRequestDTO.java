package it.blue4.recipestore.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.IncomingIngredient;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class CreateRecipeRequestDTO {
    private final String title;
    private final String description;
    private final String instructions;
    private final int numberOfServings;
    private final List<IngredientDTO> ingredients;

    @JsonCreator
    public CreateRecipeRequestDTO(String title, String description, String instructions, int numberOfServings, List<IngredientDTO> ingredients) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.numberOfServings = numberOfServings;
        this.ingredients = ingredients;
    }

    static class IngredientDTO {
        private final String name;
        private final String type;
        private final BigDecimal amount;
        private final String unit;

        public IncomingIngredient toIncomingIngredient() {
            return new IncomingIngredient(name, type, amount, unit);
        }

        @JsonCreator
        public IngredientDTO(String name, String type, BigDecimal amount, String unit) {
            this.name = name;
            this.type = type;
            this.amount = amount;
            this.unit = unit;
        }
    }

    public CreateRecipeRequest toDomainRequest() {
        return new CreateRecipeRequest(
                title,
                description,
                instructions,
                numberOfServings,
                ingredients == null ? Collections.emptyList() : ingredients.stream().map(IngredientDTO::toIncomingIngredient).toList()
        );
    }
}

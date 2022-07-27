package it.blue4.recipestore.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;

import java.util.Collections;
import java.util.List;

public class CreateRecipeRequestDTO {
    private final String title;
    private final String description;
    private final String instructions;
    private final int numberOfServings;
    private final List<IncomingIngredientDTO> ingredients;

    @JsonCreator
    public CreateRecipeRequestDTO(String title, String description, String instructions, int numberOfServings, List<IncomingIngredientDTO> ingredients) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.numberOfServings = numberOfServings;
        this.ingredients = ingredients;
    }

    public CreateRecipeRequest toDomainRequest() {
        return new CreateRecipeRequest(
                title,
                description,
                instructions,
                numberOfServings,
                ingredients == null ? Collections.emptyList() : ingredients.stream().map(IncomingIngredientDTO::toIncomingIngredient).toList()
        );
    }
}

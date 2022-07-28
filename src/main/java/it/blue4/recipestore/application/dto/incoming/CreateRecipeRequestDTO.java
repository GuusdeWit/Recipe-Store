package it.blue4.recipestore.application.dto.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;

import java.util.Collections;
import java.util.List;

public class CreateRecipeRequestDTO {
    @Schema(
            required = true,
            maxLength = 50,
            example = "Title of the recipe"
    )
    private final String title;
    @Schema(
            required = true,
            example = "Short description of the recipe"
    )
    private final String description;
    @Schema(
            required = true,
            example = "Instructions on how to prepare the recipe"
    )
    private final String instructions;
    @Schema(
            required = true,
            example = "5",
            minimum = "1"
    )
    private final int numberOfServings;
    @Schema(
            required = true
    )
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public List<IncomingIngredientDTO> getIngredients() {
        return ingredients;
    }
}

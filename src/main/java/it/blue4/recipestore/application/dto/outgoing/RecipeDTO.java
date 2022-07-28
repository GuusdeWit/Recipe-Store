package it.blue4.recipestore.application.dto.outgoing;

import it.blue4.recipestore.domain.model.Recipe;

import java.util.List;
import java.util.UUID;

public record RecipeDTO(
        UUID id,
        String title,
        String description,
        String instructions,
        int numberOfServings,
        List<IngredientDTO> ingredients,
        boolean isVegetarian
) {

    public static RecipeDTO from(Recipe recipe) {
        return new RecipeDTO(
                recipe.getRecipeId().id(),
                recipe.getTitle().title(),
                recipe.getDescription().description(),
                recipe.getInstructions().instructions(),
                recipe.getServings().number(),
                recipe.getIngredients().stream().map(IngredientDTO::from).toList(),
                recipe.isVegetarian()
        );
    }
}

package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;

import java.util.List;

import static it.blue4.recipestore.util.ValidationUtils.requireAllNonNull;
import static it.blue4.recipestore.util.ValidationUtils.requireNonNull;

public class Recipe {
    private final RecipeId recipeId = new RecipeId();
    private final Title title;
    private final Description description;
    private final Instructions instructions;
    private final Servings servings;
    private final List<Ingredient> ingredients;

    public Recipe(Title title, Description description, Instructions instructions, Servings servings, List<Ingredient> ingredients) {
        this.title = requireNonNull(title);
        this.description = requireNonNull(description);
        this.instructions = requireNonNull(instructions);
        this.servings = requireNonNull(servings);
        this.ingredients = requireAllNonNull(ingredients);
    }

    public static Recipe from(CreateRecipeRequest request) {
        return new Recipe(
                request.getTitle(),
                request.getDescription(),
                request.getInstructions(),
                request.getServings(),
                request.getIngredients()
        );
    }

    public RecipeId getRecipeId() {
        return recipeId;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }

    public Instructions getInstructions() {
        return instructions;
    }

    public Servings getServings() {
        return servings;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}

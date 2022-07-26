package it.blue4.recipestore.domain.request;

import it.blue4.recipestore.domain.model.Description;
import it.blue4.recipestore.domain.model.Instructions;
import it.blue4.recipestore.domain.model.Servings;
import it.blue4.recipestore.domain.model.Title;
import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;

import java.util.List;

public class CreateRecipeRequest {
    private final Title title;
    private final Description description;
    private final Instructions instructions;
    private final Servings servings;
    private final List<Ingredient> ingredients;

    public CreateRecipeRequest(String title, String description, String instructions, int numberOfServings, List<IncomingIngredient> ingredients) {
        this.title = new Title(title);
        this.description = new Description(description);
        this.instructions = new Instructions(instructions);
        this.servings = new Servings(numberOfServings);
        this.ingredients = ingredients.stream().map(incomingIngredient -> new Ingredient(
                new IngredientName(incomingIngredient.name()),
                IngredientType.valueOf(incomingIngredient.type()),
                new IngredientQuantity(incomingIngredient.amount(), MeasuringUnit.valueOf(incomingIngredient.unit()))
        )).toList();
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

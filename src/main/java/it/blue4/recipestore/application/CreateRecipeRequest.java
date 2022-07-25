package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.Description;
import it.blue4.recipestore.domain.Instructions;
import it.blue4.recipestore.domain.Servings;
import it.blue4.recipestore.domain.Title;

public class CreateRecipeRequest {
    private Title title;
    private Description description;
    private Instructions instructions;
    private Servings servings;

    public CreateRecipeRequest(String title, String description, String instructions, int numberOfServings) {
        this.title = new Title(title);
        this.description = new Description(description);
        this.instructions = new Instructions(instructions);
        this.servings = new Servings(numberOfServings);
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
}

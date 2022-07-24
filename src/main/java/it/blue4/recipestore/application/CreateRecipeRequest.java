package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.Description;
import it.blue4.recipestore.domain.Instructions;
import it.blue4.recipestore.domain.Title;

public class CreateRecipeRequest {
    private Title title;
    private Description description;
    private Instructions instructions;

    public CreateRecipeRequest(String title, String description, String instructions) {
        this.title = new Title(title);
        this.description = new Description(description);
        this.instructions = new Instructions(instructions);
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
}

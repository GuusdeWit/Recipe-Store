package it.blue4.recipestore.domain;

public class Recipe {
    private Title title;
    private Description description;
    private Instructions instructions;
    private Servings servings;

    public Recipe(Title title, Description description, Instructions instructions, Servings servings) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.servings = servings;
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

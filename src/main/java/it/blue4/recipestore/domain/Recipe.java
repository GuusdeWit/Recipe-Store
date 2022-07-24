package it.blue4.recipestore.domain;

public class Recipe {
    private Title title;
    private Description description;
    private Instructions instructions;

    public Recipe(Title title, Description description, Instructions instructions) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
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

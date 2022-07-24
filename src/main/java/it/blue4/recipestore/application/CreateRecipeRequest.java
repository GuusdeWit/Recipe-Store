package it.blue4.recipestore.application;

public class CreateRecipeRequest {
    private String title;
    private String description;
    private String instructions;

    public CreateRecipeRequest(String title, String description, String instructions) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
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
}

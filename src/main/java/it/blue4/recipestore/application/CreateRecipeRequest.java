package it.blue4.recipestore.application;

public class CreateRecipeRequest {
    private String title;
    private String description;

    public CreateRecipeRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

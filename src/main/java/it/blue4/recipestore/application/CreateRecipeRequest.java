package it.blue4.recipestore.application;

public class CreateRecipeRequest {
    private String title;

    public CreateRecipeRequest(String title) {

        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

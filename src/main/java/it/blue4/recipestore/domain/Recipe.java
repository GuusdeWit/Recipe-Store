package it.blue4.recipestore.domain;

public class Recipe {
    private Title title;

    public Recipe(Title title) {
        this.title = title;
    }

    public Title getTitle() {
        return title;
    }
}

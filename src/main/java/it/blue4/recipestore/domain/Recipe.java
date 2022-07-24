package it.blue4.recipestore.domain;

public class Recipe {
    private Title title;
    private Description description;

    public Recipe(Title title, Description description) {
        this.title = title;
        this.description = description;
    }

    public Title getTitle() {
        return title;
    }

    public Description getDescription() {
        return description;
    }
}

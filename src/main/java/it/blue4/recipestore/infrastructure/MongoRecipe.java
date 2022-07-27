package it.blue4.recipestore.infrastructure;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document("recipe")
public class MongoRecipe {
    @Id
    private final UUID id;
    private final String title;
    private final String description;
    private final String instructions;
    private final int numberOfServings;
    private final List<MongoIngredient> ingredients;
    private final boolean vegetarian;

    public MongoRecipe(
            UUID id,
            String title,
            String description,
            String instructions,
            int numberOfServings,
            List<MongoIngredient> ingredients,
            boolean vegetarian) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.numberOfServings = numberOfServings;
        this.ingredients = ingredients;
        this.vegetarian = vegetarian;
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

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public List<MongoIngredient> getIngredients() {
        return ingredients;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }
}

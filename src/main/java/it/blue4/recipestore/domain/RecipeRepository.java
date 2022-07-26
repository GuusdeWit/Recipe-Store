package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;

public interface RecipeRepository {
    void persist(Recipe recipe);
}

package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;

import java.util.Optional;

public interface RecipeRepository {
    void persist(Recipe recipe);

    Optional<Recipe> retrieveById(RecipeId recipeId);
}

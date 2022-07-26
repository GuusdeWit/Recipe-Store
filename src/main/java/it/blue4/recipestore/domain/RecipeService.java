package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;

public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository repository) {
        recipeRepository = repository;
    }

    public void create(CreateRecipeRequest request) {
        Recipe recipe = new Recipe(request.getTitle(), request.getDescription(), request.getInstructions(), request.getServings(), request.getIngredients());
        recipeRepository.persist(recipe);
    }
}

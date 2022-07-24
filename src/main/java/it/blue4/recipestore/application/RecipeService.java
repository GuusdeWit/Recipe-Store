package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.Recipe;
import it.blue4.recipestore.domain.RecipeRepository;

public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository repository) {
        recipeRepository = repository;
    }

    public void create(CreateRecipeRequest request) {
        Recipe recipe = new Recipe(request.getTitle(), request.getDescription(), request.getInstructions());
        recipeRepository.persist(recipe);
    }
}

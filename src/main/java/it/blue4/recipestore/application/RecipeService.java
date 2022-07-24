package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.Description;
import it.blue4.recipestore.domain.Recipe;
import it.blue4.recipestore.domain.RecipeRepository;
import it.blue4.recipestore.domain.Title;

public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository repository) {
        recipeRepository = repository;
    }

    public void create(CreateRecipeRequest request) {
        Title title = new Title(request.getTitle());
        Description description = new Description(request.getDescription());
        Instructions instructions = new Instructions(request.getInstructions());
        Recipe recipe = new Recipe(title, description, instructions);
        recipeRepository.persist(recipe);
    }
}

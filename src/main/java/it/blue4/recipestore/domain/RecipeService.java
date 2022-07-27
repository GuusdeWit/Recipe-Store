package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveRecipeRequest;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository repository) {
        recipeRepository = repository;
    }

    public void create(CreateRecipeRequest request) {
        Recipe recipe = Recipe.from(request);
        recipeRepository.persist(recipe);
    }

    public Recipe retrieve(RetrieveRecipeRequest request) {
        RecipeId id = new RecipeId(request.id());
        return recipeRepository.retrieveById(id).orElseThrow(NotFoundException::new);
    }
}

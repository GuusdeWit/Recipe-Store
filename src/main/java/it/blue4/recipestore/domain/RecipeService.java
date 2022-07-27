package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.DeleteRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveOneRecipeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Recipe retrieve(RetrieveOneRecipeRequest request) {
        RecipeId id = new RecipeId(request.id());
        return recipeRepository.retrieveById(id).orElseThrow(NotFoundException::new);
    }

    public List<Recipe> retrieveAll() {
        return recipeRepository.retrieveAll();
    }

    public void delete(DeleteRecipeRequest request) {
        recipeRepository.delete(new RecipeId(request.id()));
    }
}

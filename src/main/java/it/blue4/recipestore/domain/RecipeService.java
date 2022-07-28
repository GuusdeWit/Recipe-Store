package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.DeleteRecipeRequest;
import it.blue4.recipestore.domain.request.FilteredRetrieveRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveOneRecipeRequest;
import it.blue4.recipestore.domain.request.filter.Filter;
import it.blue4.recipestore.domain.request.filter.IngredientsFilter;
import it.blue4.recipestore.domain.request.filter.InstructionsContainsFilter;
import it.blue4.recipestore.domain.request.filter.ServingsFilter;
import it.blue4.recipestore.domain.request.filter.VegetarianFilter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public List<Recipe> retrieveAllFilteredBy(FilteredRetrieveRecipeRequest request) {
        List<? extends Filter> filters = Stream.of(
                        request.numberOfServings().map(ServingsFilter::new),
                        request.instructionContains().map(InstructionsContainsFilter::new),
                        request.isVegetarian().map(VegetarianFilter::new),
                        Optional.of(new IngredientsFilter(
                                request.ingredientInclusions().orElse(Collections.emptyList()),
                                request.ingredientExclusions().orElse(Collections.emptyList()))
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return recipeRepository.retrieveAllFilteredBy(filters);
    }

}

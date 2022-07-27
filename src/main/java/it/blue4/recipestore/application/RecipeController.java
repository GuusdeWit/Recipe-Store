package it.blue4.recipestore.application;

import it.blue4.recipestore.application.dto.CreateRecipeRequestDTO;
import it.blue4.recipestore.application.dto.Error;
import it.blue4.recipestore.application.dto.RecipeDTO;
import it.blue4.recipestore.domain.NotFoundException;
import it.blue4.recipestore.domain.RecipeService;
import it.blue4.recipestore.domain.ValidationException;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveOneRecipeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping()
    public ResponseEntity<Void> createRecipe(@RequestBody CreateRecipeRequestDTO body) {
        CreateRecipeRequest request = body.toDomainRequest();
        recipeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<Recipe> recipes = recipeService.retrieveAll();
        return ResponseEntity.ok(recipes.stream().map(RecipeDTO::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable UUID id) {
        RetrieveOneRecipeRequest request = new RetrieveOneRecipeRequest(id);
        Recipe recipe = recipeService.retrieve(request);
        return ResponseEntity.ok(RecipeDTO.from(recipe));
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<Error> validationExceptionHandler() {
        return ResponseEntity.badRequest().body(new Error("Validation error when processing request"));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Error> notFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("The requested data was not found"));
    }
}

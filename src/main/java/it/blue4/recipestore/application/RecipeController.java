package it.blue4.recipestore.application;

import it.blue4.recipestore.application.dto.CreateRecipeRequestDTO;
import it.blue4.recipestore.application.dto.Error;
import it.blue4.recipestore.application.dto.RecipeDTO;
import it.blue4.recipestore.domain.NotFoundException;
import it.blue4.recipestore.domain.RecipeService;
import it.blue4.recipestore.domain.ValidationException;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveRecipeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/recipes")
    public ResponseEntity<Void> createRecipe(@RequestBody CreateRecipeRequestDTO body) {
        CreateRecipeRequest request = body.toDomainRequest();
        recipeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable UUID id) {
        RetrieveRecipeRequest request = new RetrieveRecipeRequest(id);
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

package it.blue4.recipestore.application;

import it.blue4.recipestore.domain.RecipeService;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}

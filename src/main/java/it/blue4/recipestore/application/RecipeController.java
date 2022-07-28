package it.blue4.recipestore.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.blue4.recipestore.application.dto.incoming.CreateRecipeRequestDTO;
import it.blue4.recipestore.application.dto.outgoing.Error;
import it.blue4.recipestore.application.dto.outgoing.RecipeDTO;
import it.blue4.recipestore.domain.NotFoundException;
import it.blue4.recipestore.domain.RecipeService;
import it.blue4.recipestore.domain.ValidationException;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.request.CreateRecipeRequest;
import it.blue4.recipestore.domain.request.DeleteRecipeRequest;
import it.blue4.recipestore.domain.request.FilteredRetrieveRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveOneRecipeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping()
    @Operation(
            summary = "Create a new recipe",
            description = "This endpoint will create a new recipe based on the provided input",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(schema = @Schema(implementation = Error.class), mediaType = "application/json"))
            }
    )
    public ResponseEntity<Void> createRecipe(@RequestBody CreateRecipeRequestDTO body) {
        CreateRecipeRequest request = body.toDomainRequest();
        recipeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    @Operation(
            summary = "Get all recipes",
            description = "This endpoint will retrieve all recipes, filtered on the provided filters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = RecipeDTO.class)),
                                    mediaType = "application/json")),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(schema = @Schema(implementation = Error.class), mediaType = "application/json"))
            }
    )
    public ResponseEntity<List<RecipeDTO>> getAllRecipesWithFilters(
            @RequestParam
            @Parameter(description = "Filter based on the number of servings equal to provided value")
            Optional<Integer> numberOfServings,
            @RequestParam
            @Parameter(description = "Filter based on the instructions containing the provided text")
            Optional<String> instructionContains,
            @RequestParam
            @Parameter(description = "Filter based on including ingredients. Recipes will contain all provided ingredients")
            Optional<List<String>> includeIngredients,
            @RequestParam
            @Parameter(description = "Filter based on excluding ingredients. Recipes will contain non of the provided ingredients")
            Optional<List<String>> excludeIngredients,
            @RequestParam
            @Parameter(description = "Filter based on whether the dish is vegetarian. This is based on the ingredients in the recipe")
            Optional<Boolean> isVegetarian
    ) {
        FilteredRetrieveRecipeRequest request = new FilteredRetrieveRecipeRequest(
                numberOfServings,
                instructionContains,
                includeIngredients,
                excludeIngredients,
                isVegetarian
        );
        List<Recipe> recipes = recipeService.retrieveAllFilteredBy(request);
        return ResponseEntity.ok(recipes.stream().map(RecipeDTO::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a recipe",
            description = "This endpoint will retrieve a recipe with the provided id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(implementation = RecipeDTO.class),
                                    mediaType = "application/json")),

                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(schema = @Schema(implementation = Error.class), mediaType = "application/json"))
            }
    )
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable UUID id) {
        RetrieveOneRecipeRequest request = new RetrieveOneRecipeRequest(id);
        Recipe recipe = recipeService.retrieve(request);
        return ResponseEntity.ok(RecipeDTO.from(recipe));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a recipe",
            description = "This endpoint will delete a recipe with the provided id"
    )
    public ResponseEntity<Void> deleteRecipeById(@PathVariable UUID id) {
        DeleteRecipeRequest request = new DeleteRecipeRequest(id);
        recipeService.delete(request);
        return ResponseEntity.ok().build();
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

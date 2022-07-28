package it.blue4.recipestore.domain.request;

import java.util.List;
import java.util.Optional;

public record FilteredRetrieveRecipeRequest(
        Optional<Integer> numberOfServings,
        Optional<String> instructionContains,
        Optional<List<String>> ingredientInclusions,
        Optional<List<String>> ingredientExclusions,
        Optional<Boolean> isVegetarian
) {
}

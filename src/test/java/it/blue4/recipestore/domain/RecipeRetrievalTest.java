package it.blue4.recipestore.domain;

import it.blue4.recipestore.domain.model.Description;
import it.blue4.recipestore.domain.model.Instructions;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.model.Servings;
import it.blue4.recipestore.domain.model.Title;
import it.blue4.recipestore.domain.model.ingredient.Ingredient;
import it.blue4.recipestore.domain.model.ingredient.IngredientName;
import it.blue4.recipestore.domain.model.ingredient.IngredientQuantity;
import it.blue4.recipestore.domain.model.ingredient.IngredientType;
import it.blue4.recipestore.domain.model.ingredient.MeasuringUnit;
import it.blue4.recipestore.domain.request.FilteredRetrieveRecipeRequest;
import it.blue4.recipestore.domain.request.RetrieveOneRecipeRequest;
import it.blue4.recipestore.domain.request.filter.Filter;
import it.blue4.recipestore.domain.request.filter.IngredientsFilter;
import it.blue4.recipestore.domain.request.filter.InstructionsContainsFilter;
import it.blue4.recipestore.domain.request.filter.ServingsFilter;
import it.blue4.recipestore.domain.request.filter.VegetarianFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class RecipeRetrievalTest {

    private final RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    private final RecipeService recipeService = new RecipeService(repository);

    @Captor
    private ArgumentCaptor<List<Filter>> filtersCaptor;

    private final Recipe defaultRecipe = new Recipe(
            new Title("title"),
            new Description("description"),
            new Instructions("instructions"),
            new Servings(2),
            List.of(
                    new Ingredient(
                            new IngredientName("first"),
                            IngredientType.PLANT_BASED,
                            new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)
                    )
            )
    );

    AutoCloseable closeable;

    @BeforeEach
    void initMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        closeable.close();
    }

    @Nested
    class RetrieveOneTests {

        @Test
        @DisplayName("retrieve should return a Recipe from the repository")
        void retrieveShouldReturnRecipe() {
            // Given
            UUID id = defaultRecipe.getRecipeId().id();
            RetrieveOneRecipeRequest request = new RetrieveOneRecipeRequest(id);

            // When
            Mockito.when(repository.retrieveById(new RecipeId(id))).thenReturn(Optional.of(defaultRecipe));
            Recipe result = recipeService.retrieve(request);

            // Then
            assertThat(result).isEqualTo(defaultRecipe);
        }

        @Test
        @DisplayName("retrieve should throw a NotFoundException if repository returns empty optional")
        void retrieveShouldThrowIfNotFound() {
            // Given
            UUID id = defaultRecipe.getRecipeId().id();
            RetrieveOneRecipeRequest request = new RetrieveOneRecipeRequest(id);

            // When
            Mockito.when(repository.retrieveById(any())).thenReturn(Optional.empty());

            // Then
            assertThrows(NotFoundException.class, () -> recipeService.retrieve(request));
        }

        @Test
        @DisplayName("retrieve should throw a ValidationException if id is null")
        void retrieveShouldThrowIfNoIdProvided() {
            // Given
            RetrieveOneRecipeRequest request = new RetrieveOneRecipeRequest(null);

            // Then
            assertThrows(ValidationException.class, () -> recipeService.retrieve(request));
        }
    }

    @Nested
    class RetrieveMultipleTests {
        @Test
        @DisplayName("retrieveAll should retrieve all from the repository")
        void retrieveAllShouldReturnList() {
            // Given
            List<Recipe> recipes = List.of(defaultRecipe);

            // When
            Mockito.when(repository.retrieveAll()).thenReturn(recipes);
            List<Recipe> result = recipeService.retrieveAll();

            // Then
            assertThat(result).containsExactlyElementsOf(recipes);
        }

        @Test
        @DisplayName("retrieveAllFilteredBy should return list provided by repository")
        void retrieveShouldReturnList() {
            // Given
            FilteredRetrieveRecipeRequest request = new FilteredRetrieveRecipeRequest(
                    Optional.of(5),
                    Optional.of("text in instruction"),
                    Optional.of(List.of("ing1 in recipe", "ing2 in recipe")),
                    Optional.of(List.of("ing3 not in recipe", "ing4 not in recipe")),
                    Optional.of(true)
            );

            // When
            Mockito.when(repository.retrieveAllFilteredBy(any())).thenReturn(List.of(defaultRecipe));
            List<Recipe> result = recipeService.retrieveAllFilteredBy(request);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(defaultRecipe);
        }

        @ParameterizedTest
        @DisplayName("retrieveAllFilteredBy should pass filters to the repository")
        @MethodSource("provideFilteredRequests")
        void retrieveShouldFilterOnNumberOfServings(FilteredRetrieveRecipeRequest request, List<Filter> expectedFilters) {
            // When
            recipeService.retrieveAllFilteredBy(request);

            // Then
            verify(repository).retrieveAllFilteredBy(filtersCaptor.capture());
            List<Filter> filters = filtersCaptor.getValue();

            assertThat(filters).containsExactlyElementsOf(expectedFilters);
        }

        static Stream<Arguments> provideFilteredRequests() {
            Optional<Integer> numberOfServings = Optional.of(5);
            Optional<String> text = Optional.of("text in instruction");
            Optional<List<String>> ingredientInclusions = Optional.of(List.of("ing1 in recipe", "ing2 in recipe"));
            Optional<List<String>> ingredientExclusions = Optional.of(List.of("ing3 not in recipe", "ing4 not in recipe"));
            Optional<Boolean> isVegetarian = Optional.of(true);

            return Stream.of(
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    numberOfServings,
                                    text,
                                    ingredientInclusions,
                                    ingredientExclusions,
                                    isVegetarian
                            ),
                            List.of(
                                    new ServingsFilter(numberOfServings.get()),
                                    new InstructionsContainsFilter(text.get()),
                                    new VegetarianFilter(true),
                                    new IngredientsFilter(ingredientInclusions.get(), ingredientExclusions.get())
                            )
                    ),
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    Optional.empty(),
                                    text,
                                    ingredientInclusions,
                                    ingredientExclusions,
                                    isVegetarian
                            ),
                            List.of(
                                    new InstructionsContainsFilter(text.get()),
                                    new VegetarianFilter(true),
                                    new IngredientsFilter(ingredientInclusions.get(), ingredientExclusions.get())
                            )
                    ),
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    Optional.empty(),
                                    text,
                                    Optional.empty(),
                                    Optional.empty(),
                                    isVegetarian
                            ),
                            List.of(
                                    new InstructionsContainsFilter(text.get()),
                                    new VegetarianFilter(true),
                                    new IngredientsFilter(emptyList(), emptyList())
                            )
                    ),
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    ingredientExclusions,
                                    isVegetarian
                            ),
                            List.of(
                                    new VegetarianFilter(true),
                                    new IngredientsFilter(emptyList(), ingredientExclusions.get())
                            )
                    ),
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    isVegetarian
                            ),
                            List.of(
                                    new VegetarianFilter(true),
                                    new IngredientsFilter(emptyList(), emptyList())
                            )
                    ),
                    Arguments.of(new FilteredRetrieveRecipeRequest(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty()
                            ),
                            List.of(
                                    new IngredientsFilter(emptyList(), emptyList())
                            )
                    )
            );
        }
    }
}

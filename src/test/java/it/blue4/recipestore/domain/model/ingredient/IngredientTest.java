package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientTest {
    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void constructor_shouldNotAllowNullParameters(IngredientName name, IngredientType type, IngredientQuantity quantity) {
        assertThrows(ValidationException.class, () -> new Ingredient(name, type, quantity));
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(null, IngredientType.MEAT, new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)),
                Arguments.of(new IngredientName("name"), null, new IngredientQuantity(BigDecimal.ONE, MeasuringUnit.PIECE)),
                Arguments.of(new IngredientName("name"), IngredientType.MEAT, null)
        );
    }
}
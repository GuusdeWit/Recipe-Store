package it.blue4.recipestore.domain.model.ingredient;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientQuantityTest {

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void constructor_withInvalidArgumentsShouldThrowValidationException(BigDecimal amount, MeasuringUnit unit) {
        assertThrows(ValidationException.class, () -> new IngredientQuantity(amount, unit));
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(null, MeasuringUnit.PIECE),
                Arguments.of(BigDecimal.ONE, null),
                Arguments.of(BigDecimal.ZERO, MeasuringUnit.PIECE),
                Arguments.of(BigDecimal.valueOf(-2), MeasuringUnit.PIECE)
        );
    }

}
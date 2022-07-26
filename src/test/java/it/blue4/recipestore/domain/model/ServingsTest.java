package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ServingsTest {
    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    void constructor_withInvalidArgumentsShouldThrowValidationException(int number) {
        assertThrows(ValidationException.class, () -> new Servings(number));
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(-1),
                Arguments.of(-999)
        );
    }
}
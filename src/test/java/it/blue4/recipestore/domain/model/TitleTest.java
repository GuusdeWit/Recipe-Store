package it.blue4.recipestore.domain.model;

import it.blue4.recipestore.domain.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TitleTest {
    @Test
    @DisplayName("constructing a title with 50 characters is allowed")
    void constructingTitleAtLimitIsAllowed() {
        String longAllowedText = "This title is exactly 50 chars long, hence allowed";
        Title title = new Title(longAllowedText);
        assertThat(title.title()).isEqualTo(longAllowedText);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArguments")
    @NullSource
    void constructor_withInvalidArgumentsShouldThrowValidationException(String title) {
        assertThrows(ValidationException.class, () -> new Title(title));
    }

    static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of("This title is exactly 51 chars long, so not allowed"),
                Arguments.of("This title is waaaaaaaaaaaay longer than the required limit, so it should not be allowed in the constructor")
        );
    }
}

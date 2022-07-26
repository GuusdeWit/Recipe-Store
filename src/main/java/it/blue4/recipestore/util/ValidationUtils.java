package it.blue4.recipestore.util;

import it.blue4.recipestore.domain.ValidationException;

import java.util.List;

public class ValidationUtils {
    private ValidationUtils() {
    }

    public static <T> T requireNonNull(T object) throws ValidationException {
        if (object == null) {
            throw new ValidationException();
        }
        return object;
    }

    public static <T> List<T> requireAllNonNull(List<T> list) throws ValidationException {
        requireNonNull(list);
        list.forEach(ValidationUtils::requireNonNull);
        return list;
    }
}

package it.blue4.recipestore;

import java.math.BigDecimal;
import java.util.UUID;

public interface TestPersistService {
    void persistOneWithId(UUID id);

    boolean dataStoreDoesNotContainElementWithId(UUID id);

    boolean dataStoreIsEmpty();

    boolean dataStoreContainsExactly(int numberOfElements);

    void removeAllData();

    String title = "title";
    String description = "description";
    String instructions = "instructions";
    int numberOfServings = 2;
    String ingredientName = "name";
    String ingredientType = "MEAT";

    BigDecimal ingredientAmount = BigDecimal.valueOf(1.5);

    String ingredientUnit = "PIECE";
}

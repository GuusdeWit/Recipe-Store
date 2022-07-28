package it.blue4.recipestore;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TestPersistService {
    void persistOneWithId(UUID id);

    void persistMultiple(List<UUID> ids);

    boolean dataStoreDoesNotContainElementWithId(UUID id);

    boolean dataStoreIsEmpty();

    boolean dataStoreContainsExactly(int numberOfElements);

    void removeAllData();

    String title = "title";
    String description = "description";
    String instructions = "recipe instructions";
    int numberOfServings = 2;
    String ingredientName = "sausage";
    String ingredientType = "MEAT";

    BigDecimal ingredientAmount = BigDecimal.valueOf(1.5);

    String ingredientUnit = "PIECE";
    boolean vegetarian = false;
}

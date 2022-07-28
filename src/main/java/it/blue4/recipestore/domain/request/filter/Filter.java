package it.blue4.recipestore.domain.request.filter;

public sealed interface Filter permits
        IngredientsFilter, InstructionsContainsFilter, ServingsFilter, VegetarianFilter {
}


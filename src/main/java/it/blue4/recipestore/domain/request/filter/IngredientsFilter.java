package it.blue4.recipestore.domain.request.filter;

import java.util.List;

public record IngredientsFilter(List<String> include, List<String> exclude) implements Filter {
}

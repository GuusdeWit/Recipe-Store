package it.blue4.recipestore.infrastructure;

import it.blue4.recipestore.domain.RecipeRepository;
import it.blue4.recipestore.domain.model.Recipe;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoRecipeRepository implements RecipeRepository {

    private final MongoTemplate mongoTemplate;

    public MongoRecipeRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void persist(Recipe recipe) {
        mongoTemplate.save(new MongoRecipe(
                        recipe.getRecipeId().id(),
                        recipe.getTitle().title(),
                        recipe.getDescription().description(),
                        recipe.getInstructions().instructions(),
                        recipe.getServings().number(),
                        recipe.getIngredients().stream().map(in -> new MongoIngredient(
                                in.name().name(),
                                in.type().name(),
                                in.quantity().amount(),
                                in.quantity().unit().name()
                        )).toList(),
                        recipe.isVegetarian()
                )
        );
    }
}

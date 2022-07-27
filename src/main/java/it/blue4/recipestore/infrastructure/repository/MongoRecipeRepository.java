package it.blue4.recipestore.infrastructure.repository;

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
        mongoTemplate.save(MongoRecipe.from(recipe));
    }
}

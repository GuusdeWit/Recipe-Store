package it.blue4.recipestore.infrastructure.repository;

import it.blue4.recipestore.domain.RecipeRepository;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Recipe> retrieveById(RecipeId recipeId) {
        MongoRecipe data = mongoTemplate.findById(recipeId.id(), MongoRecipe.class);
        return Optional.ofNullable(data).map(MongoRecipe::toDomainRecipe);
    }

    @Override
    public List<Recipe> retrieveAll() {
        List<MongoRecipe> data = mongoTemplate.findAll(MongoRecipe.class);
        return data.stream().map(MongoRecipe::toDomainRecipe).toList();
    }

    @Override
    public void delete(RecipeId recipeId) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(recipeId.id())), MongoRecipe.class);
    }
}

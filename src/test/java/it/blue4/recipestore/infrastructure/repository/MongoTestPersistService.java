package it.blue4.recipestore.infrastructure.repository;

import it.blue4.recipestore.TestPersistService;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.UUID;

@TestComponent
public class MongoTestPersistService implements TestPersistService {

    private final MongoTemplate mongoTemplate;

    public MongoTestPersistService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void persistOneWithId(UUID id) {
        MongoRecipe mongoRecipe = new MongoRecipe(
                id,
                title,
                description,
                instructions,
                numberOfServings,
                List.of(
                        new MongoIngredient(ingredientName, ingredientType, ingredientAmount, ingredientUnit)
                ),
                false
        );
        mongoTemplate.insert(mongoRecipe);
    }

    @Override
    public boolean dataStoreDoesNotContainElementWithId(UUID id) {
        var data = mongoTemplate.findById(id, MongoRecipe.class);
        return data != null;
    }

    @Override
    public boolean dataStoreIsEmpty() {
        var data = mongoTemplate.findAll(MongoRecipe.class);
        return data.isEmpty();
    }

    @Override
    public boolean dataStoreContainsExactly(int numberOfElements) {
        var data = mongoTemplate.findAll(MongoRecipe.class);
        return data.size() == numberOfElements;
    }

    @Override
    public void removeAllData() {
        mongoTemplate.dropCollection(MongoRecipe.class);
    }
}

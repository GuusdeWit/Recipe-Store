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
                vegetarian
        );
        mongoTemplate.insert(mongoRecipe);
    }

    @Override
    public void persistMultiple(List<UUID> ids) {
        MongoRecipe mongoRecipe1 = new MongoRecipe(
                ids.get(0),
                title,
                description,
                "These are the instructions",
                2,
                List.of(
                        new MongoIngredient(
                                "sausage",
                                ingredientType,
                                ingredientAmount,
                                ingredientUnit)
                ),
                false
        );
        mongoTemplate.insert(mongoRecipe1);

        MongoRecipe mongoRecipe2 = new MongoRecipe(
                ids.get(1),
                title,
                description,
                "These are the instructions",
                3,
                List.of(
                        new MongoIngredient(
                                "cheese",
                                ingredientType,
                                ingredientAmount,
                                ingredientUnit)
                ),
                true
        );
        mongoTemplate.insert(mongoRecipe2);

        MongoRecipe mongoRecipe3 = new MongoRecipe(
                ids.get(2),
                title,
                description,
                "these instructions are hard to execute",
                3,
                List.of(
                        new MongoIngredient(
                                "cheese",
                                ingredientType,
                                ingredientAmount,
                                ingredientUnit),
                        new MongoIngredient(
                                "fish",
                                ingredientType,
                                ingredientAmount,
                                ingredientUnit)
                ),
                false
        );
        mongoTemplate.insert(mongoRecipe3);
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

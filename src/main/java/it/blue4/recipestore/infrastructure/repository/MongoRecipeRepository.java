package it.blue4.recipestore.infrastructure.repository;

import it.blue4.recipestore.domain.RecipeRepository;
import it.blue4.recipestore.domain.model.Recipe;
import it.blue4.recipestore.domain.model.RecipeId;
import it.blue4.recipestore.domain.request.filter.Filter;
import it.blue4.recipestore.domain.request.filter.IngredientsFilter;
import it.blue4.recipestore.domain.request.filter.InstructionsContainsFilter;
import it.blue4.recipestore.domain.request.filter.ServingsFilter;
import it.blue4.recipestore.domain.request.filter.VegetarianFilter;
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

    @Override
    public List<Recipe> retrieveAllFilteredBy(List<? extends Filter> filters) {
        Query query = new Query();
        filters.stream()
                .map(this::convertFilterToCriteria)
                .forEach(query::addCriteria);
        List<MongoRecipe> data = mongoTemplate.find(query, MongoRecipe.class);
        return data.stream().map(MongoRecipe::toDomainRecipe).toList();
    }

    private Criteria convertFilterToCriteria(Filter filter) {
        if (filter instanceof VegetarianFilter vegetarianFilter) {
            return convertFilterToCriteria(vegetarianFilter);
        } else if (filter instanceof InstructionsContainsFilter instructionsContainsFilter) {
            return convertFilterToCriteria(instructionsContainsFilter);
        } else if (filter instanceof IngredientsFilter ingredientsFilter) {
            return convertFilterToCriteria(ingredientsFilter);
        } else {
            return convertFilterToCriteria((ServingsFilter) filter);
        }
    }

    private Criteria convertFilterToCriteria(VegetarianFilter filter) {
        return Criteria.where("vegetarian").is(filter.isVegetarian());
    }

    private Criteria convertFilterToCriteria(ServingsFilter filter) {
        return Criteria.where("numberOfServings").is(filter.numberOfServings());
    }

    private Criteria convertFilterToCriteria(InstructionsContainsFilter filter) {
        return Criteria.where("instructions").regex(filter.text());
    }

    private Criteria convertFilterToCriteria(IngredientsFilter filter) {
        Criteria exclusionCriteria = Criteria.where("ingredients.name").not().in(filter.exclude());
        Criteria inclusionCriteria = filter.include().isEmpty() ? new Criteria() :
                Criteria.where("ingredients.name").all(filter.include());
        return new Criteria().andOperator(
                exclusionCriteria,
                inclusionCriteria
        );
    }
}

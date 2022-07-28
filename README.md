# Recipe-Store
Simple REST API for storing and managing your favourite recipes

## Installation
Use `docker-compose` to build and start the application:
```
docker-compose up -d --build
```

If you want to run the application from the command line or from an IDE, be sure to set the environment variable `MONGODB_URI` to a valid mongo instance (e.g. `mongodb://localhost:27017` if you are also running the docker compose)

## Usage
By default, the application will start a web server on localhost with port `8085` (configured in the [Docker compose](docker-compose.yml)
file).

An open api specification describing the endpoints is located at [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs) and there is also a Swagger ui:
[http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)

The application allows you to manage your favourite recipes by calling the REST endpoints, and there is the possibility to filter recipes based on query parameters (see the [Swagger docs](http://localhost:8085/swagger-ui.html)).

The docker compose also provides an instance of mongo-express, so you can inspect the stored data directly via [http://localhost:8081/](http://localhost:8081/)

## Design
### Business requirements
The following requirements were provided:
* The application should be able to add, update, retrieve and delete recipes.
* The application should be able to filter on one or more of the following criteria:
  - Whether the dish is vegetarian
  - The number of servings
  - Specific ingredients (either include or exclude)
  - Text search within the instructions.

On top of these I added the following requirements
* Title of a recipe cannot be longer than 50 characters.
* Ingredients could be measured in decimals and in the following units:
    - cm, gram, ml, teaspoon, tablespoon, piece
* Ingredients can be of the following type:
    - meat, fish, dairy, plant-based, spice
* Recipes are vegetarian if they don't contain meat or fish.

### Implementation details
I chose to write the application with Java 18 and Spring Boot. As a datastore, the application makes use of MongoDB. We use a document database, because in my opinion it suits the business requirements well:
* A recipe should contain the complete information about its instructions and ingredients.
* Although ingredients can be reused in different recipes, the context is not always the same: ingredients can be prepared in different ways, or should be obtained in certain forms.

Based on these points, using a document database feels most natural. That being said, the application is written in a domain-driven way, where the persistence is secondary. 
It is entirely possible to write a different implementation of the infrastructure, to support a different data storage solution.

### Tests
The tests are divided into three parts:
* `domain`: these contain unit tests to ensure business rules and that domain models are always valid.
* `infrastructure`: these contain integration tests for the provided data store implementation.
* `component`: these contain full integration tests of the application, making sure everything is wired correctly. 
There are only a couple of tests per endpoint, as all business rules are rigorously tested in the domain and infrastructure layers.

## Possible improvements
Due to time constraints, I was unfortunately not able to finish one of the requirements:
* Updating recipes (the implementation would be similar to the creation of recipes)

Furthermore, I left the following considerations out of scope, but could be seen as future improvements:
* Authentication, authorization
* Users (each user could have their own recipes)
* Logging, metrics and tracing
* Better error messages based on user input (the ones provided are rather generic)
# Spring Boot "Recipe Service"
- This is a sample Java / Maven / Spring Boot (version 2.7.4) application
- Uses In Memory H2 database 
# How to Run
This application is packaged as a jar. You run it using the ```java -jar``` command.
* Project uses JDK 17
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service:
```
java -jar target/recipe-service 0.0.1-SNAPSHOT
```
## Rest API Documentation using Swagger
````
http://localhost:8080/swagger
````
### Get All Recipes

````
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*'
````
### Get All Veg Recipes
````
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'veg: true'
````
### Get All Recipes that can server 4 people and have ingredient potato
````
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'servings: 4' \
  -H 'ingredients: potato'
````
### Get All Recipes with instructions containing oven, without salmon as an ingredient
````
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'instructions: oven' \
  -H 'ingredients: salmon' \
  -H 'excludeIng: true'
````
### Get All Recipes with cake in instructions
````
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'instructions: cake'
````
### Get All Recipes with cake and oven in instructions
````  
 curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'instructions: cake,oven'
````  
### Get All Recipes with potato or salmon as ingredients
````  
curl -X 'GET' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'ingredients: potato,salmon'
  
````
### Create Recipe
````
curl -X 'POST' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "r1",
  "veg": true,
  "servings": 5,
  "instructions": "cake oven",
  "ingredients": [
    {
      "name": "potato"
    }
  ]
}'

curl -X 'POST' \
  'http://localhost:8080/v1/recipes' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "r2",
  "veg": false,
  "servings": 4,
  "instructions": "cake",
  "ingredients": [
    {
      "name": "potato"
    },
    {
      "name": "salmon"
    }
  ]
}'

````

### Update Recipe
````
curl -X 'PUT' \
  'http://localhost:8080/v1/recipes/1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "r1",
  "veg": true,
  "servings": 5,
  "instructions": "cake oven bake",
  "ingredients": [
    {
      "name": "potato"
    }
  ]
}'

````

### Delete Recipe
````
curl -X 'DELETE' \
  'http://localhost:8080/v1/recipes/1' \
  -H 'accept: */*'
  
````
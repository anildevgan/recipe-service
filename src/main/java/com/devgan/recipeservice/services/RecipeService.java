package com.devgan.recipeservice.services;

import com.devgan.recipeservice.daos.Recipe;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface RecipeService {

	List<Recipe> getRecipes(Boolean veg, Long servings, String instructions, String ingredients,
	                        Boolean exclude);

	Recipe saveRecipe(Recipe recipe);

	Recipe updateRecipe(long id, Recipe updatedRecipe);

	void deleteRecipe(long id);
}

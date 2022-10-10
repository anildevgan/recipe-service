package com.devgan.recipeservice.services.impl;

import com.devgan.recipeservice.daos.Ingredient;
import com.devgan.recipeservice.daos.Recipe;
import com.devgan.recipeservice.exceptions.NotFoundException;
import com.devgan.recipeservice.repositories.RecipeRepository;
import com.devgan.recipeservice.services.RecipeService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;

	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public List<Recipe> getRecipes(Boolean veg, Long servings, String instructions,
	                               String ingredients, Boolean exclude) {
		List<Recipe> recipes = recipeRepository.findAll();
		if (Optional.ofNullable(veg).isPresent()) {
			recipes = recipes.stream().filter(r -> r.isVeg() == veg).collect(Collectors.toList());
		}
		if (Optional.ofNullable(servings).isPresent()) {
			recipes = recipes.stream().filter(r -> r.getServings() >= servings)
					.collect(Collectors.toList());
		}
		if (Optional.ofNullable(instructions).isPresent()) {
			Set<String> instructionSet = new HashSet<>(Arrays.asList(instructions.split(",")));
			recipes = recipes.stream().filter(r ->
					instructionSet.stream().allMatch(i -> r.getInstructions().contains(i))
			).collect(Collectors.toList());
		}
		if (Optional.ofNullable(ingredients).isPresent()) {
			Set<String> ingredientSet = new HashSet<>(Arrays.asList(ingredients.split(",")));
			if (Optional.ofNullable(exclude).isPresent() && Boolean.TRUE.equals(exclude)) {
				recipes = recipes.stream()
						.filter(r -> r.getIngredients().stream().map(Ingredient::getName)
								.noneMatch(ingredientSet::contains)).collect(Collectors.toList());
			} else {
				recipes = recipes.stream()
						.filter(r -> r.getIngredients().stream().map(Ingredient::getName)
								.anyMatch(ingredientSet::contains)).collect(Collectors.toList());
			}
		}
		return recipes;
	}

	@Override
	public Recipe saveRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}

	@Override
	public Recipe updateRecipe(long id, Recipe updatedRecipe) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(id);
		if (recipeOptional.isEmpty()) {
			throw new NotFoundException("Recipe Not Found to update");
		}
		Recipe recipe = recipeOptional.get();
		recipe.setIngredients(updatedRecipe.getIngredients());
		recipe.setServings(updatedRecipe.getServings());
		recipe.setName(updatedRecipe.getName());
		recipe.setInstructions(updatedRecipe.getInstructions());
		recipe.setVeg(updatedRecipe.isVeg());
		return recipeRepository.save(recipe);
	}

	@Override
	public void deleteRecipe(long id) {
		try {
			recipeRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException(e.getMessage());
		}
	}
}

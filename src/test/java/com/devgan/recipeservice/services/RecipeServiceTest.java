package com.devgan.recipeservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.devgan.recipeservice.daos.Ingredient;
import com.devgan.recipeservice.daos.Recipe;
import com.devgan.recipeservice.exceptions.NotFoundException;
import com.devgan.recipeservice.repositories.RecipeRepository;
import com.devgan.recipeservice.services.impl.RecipeServiceImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

	@Mock
	private RecipeRepository recipeRepository;

	RecipeService recipeService;

	List<Recipe> recipes;
	Recipe recipe1;
	Recipe recipe2;

	@BeforeEach
	void setUp() {
		recipeService = new RecipeServiceImpl(recipeRepository);
		Set<Ingredient> ingredients1 = new HashSet<>();
		Ingredient ingredient1 = new Ingredient(1L, "potato");
		ingredients1.add(ingredient1);
		Set<Ingredient> ingredients2 = new HashSet<>();
		Ingredient ingredient2 = new Ingredient(1L, "salmon");
		Ingredient ingredient3 = new Ingredient(1L, "potato");
		ingredients2.add(ingredient2);
		ingredients2.add(ingredient3);
		recipe1 = new Recipe(1L, "Recipe1", true, 5, "cake oven", ingredients1);
		recipe2 = new Recipe(2L, "Recipe2", false, 4, "cake", ingredients2);
		recipes=new ArrayList<>();
		recipes.add(recipe1);
		recipes.add(recipe2);
	}


	@Test
	void getRecipesAll() {
		when(recipeRepository.findAll()).thenReturn(recipes);
		List<Recipe> recipeResponse=recipeService.getRecipes(null, null, null, null, null);
		assertEquals(2, recipeResponse.size());
	}
	//Use Case 1: ALl Veg Recipies
	@Test
	void getRecipesVegTrue() {
		when(recipeRepository.findAll()).thenReturn(recipes);
		List<Recipe> recipeResponse=recipeService.getRecipes(true, null, null, null, null);
		assertEquals(1, recipeResponse.size());
	}
	@Test
	void getRecipesVegFalse() {
		when(recipeRepository.findAll()).thenReturn(recipes);
		List<Recipe> recipeResponse=recipeService.getRecipes(false, null, null, null, null);
		assertEquals(1, recipeResponse.size());
	}
	//Use Case 2: Serve 4 persons, potato as an ingredient
	// Result 2 Because both recipes can serve 4 people and have potato as ingredient
	@Test
	void getRecipesServesIngredient() {
		when(recipeRepository.findAll()).thenReturn(recipes);
		List<Recipe> recipeResponse=recipeService.getRecipes(null, 4L, null, "potato", null);
		assertEquals(2, recipeResponse.size());
	}

	//Use Case 3: Without salmon as an ingredient, oven in instructions
	@Test
	void getRecipesInstructionsIngredientExclude() {
		when(recipeRepository.findAll()).thenReturn(recipes);
		List<Recipe> recipeResponse=recipeService.getRecipes(null, null, "oven", "salmon", true);
		assertEquals(1, recipeResponse.size());
	}

	@Test
	void saveRecipe() {
		when(recipeRepository.save(ArgumentMatchers.any(Recipe.class))).thenReturn(recipe1);
		Recipe recipeResponse = recipeService.saveRecipe(recipe1);
		assertNotNull(recipeResponse);
		assertEquals(recipe1.getName(), recipeResponse.getName());
	}

	@Test
	void updateRecipeNotFoundException() {
		when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> recipeService.updateRecipe(1, recipe1));
	}

	@Test
	void updateRecipe() {
		when(recipeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(recipe1));
		when(recipeRepository.save(ArgumentMatchers.any(Recipe.class))).thenReturn(recipe1);
		Recipe recipeResponse = recipeService.updateRecipe(1, recipe1);
		assertNotNull(recipeResponse);
		assertEquals(recipe1.getName(), recipeResponse.getName());
	}

	@Test
	void deleteRecipeNotFoundException() {
		doThrow(new EmptyResultDataAccessException(0)).when(recipeRepository)
				.deleteById((ArgumentMatchers.any()));
		assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(1));
	}

	@Test
	void deleteRecipe() {
		doNothing().when(recipeRepository).deleteById((ArgumentMatchers.any()));
		recipeService.deleteRecipe(1);
		verify(recipeRepository).deleteById(ArgumentMatchers.any());
	}
}
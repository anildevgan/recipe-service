package com.devgan.recipeservice.controllers;

import com.devgan.recipeservice.daos.Recipe;
import com.devgan.recipeservice.services.RecipeService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/recipes")
public class RecipeController {

	private final RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping
	public List<Recipe> getRecipes(@RequestHeader(required = false) Boolean veg,
	                               @RequestHeader(required = false) Long servings,
	                               @RequestHeader(required = false) String instructions,
	                               @RequestHeader(required = false) String ingredients,
	                               @RequestHeader(required = false) Boolean excludeIng
	) {
		return recipeService.getRecipes(veg, servings, instructions, ingredients, excludeIng);
	}

	@PostMapping
	public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
		Recipe savedRecipe = recipeService.saveRecipe(recipe);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedRecipe.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe, @PathVariable long id) {
		Recipe savedRecipe = recipeService.updateRecipe(id, recipe);
		return ResponseEntity.ok(savedRecipe);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteRecipe(@PathVariable long id) {
		recipeService.deleteRecipe(id);
		return ResponseEntity.ok().build();
	}
}

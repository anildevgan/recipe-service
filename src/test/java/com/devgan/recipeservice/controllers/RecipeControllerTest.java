package com.devgan.recipeservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devgan.recipeservice.daos.Ingredient;
import com.devgan.recipeservice.daos.Recipe;
import com.devgan.recipeservice.exceptions.NotFoundException;
import com.devgan.recipeservice.services.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.hibernate.resource.transaction.backend.jta.internal.synchronization.ExceptionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest({RecipeController.class})
class RecipeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	RecipeController recipeController;

	@MockBean
	RecipeService recipeService;

	List<Recipe> recipes;
	Recipe recipe1;
	Recipe recipe2;

	@BeforeEach
	void setUp() {
		recipeController = new RecipeController(recipeService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
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
		recipes = new ArrayList<>();
		recipes.add(recipe1);
		recipes.add(recipe2);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getRecipesNotFound() throws Exception {
		when(recipeService.getRecipes(any(), any(), any(), any(), any()))
				.thenThrow(NotFoundException.class);
		MockHttpServletRequestBuilder
				req = MockMvcRequestBuilders.get("/v1/recipes");
		MvcResult result = mockMvc.perform(req).andExpect(status().isNotFound()).andReturn();
		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	@Test
	void getRecipes() throws Exception {
		when(recipeService.getRecipes(any(), any(), any(), any(), any())).thenReturn(recipes);
		MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/v1/recipes");
		MvcResult result = mockMvc.perform(req).andExpect(status().isOk()).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	void createRecipe() throws Exception {
		when(recipeService.saveRecipe(any())).thenReturn(recipe1);
		MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/v1/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjToJson(recipe1));
		MvcResult result = mockMvc.perform(req).andExpect(status().isCreated()).andReturn();
		assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	}

	@Test
	void updateRecipe() throws Exception {
		when(recipeService.updateRecipe(anyLong(),any())).thenReturn(recipe1);
		MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put("/v1/recipes/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjToJson(recipe1));
		MvcResult result = mockMvc.perform(req).andExpect(status().isOk()).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	void deleteRecipe() throws Exception {
		doNothing().when(recipeService).deleteRecipe(1L);
		MockHttpServletRequestBuilder req = MockMvcRequestBuilders.delete("/v1/recipes/1");
		MvcResult result = mockMvc.perform(req).andExpect(status().isOk()).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	private static String convertObjToJson(Recipe recipe) {
		String requestPayload = "";
		try {
			requestPayload = new ObjectMapper().writeValueAsString(recipe);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return requestPayload;
	}
}
package com.restOne.hello;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/Recipes")
public class RecipeController {

	private static final Logger log = LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private RecipeRepository repository;

	@RequestMapping(method = RequestMethod.POST)
	public String createRecipe(@RequestBody Recipe recipe) {

		log.info("start creating recipe");

		repository.save(recipe);

		return recipe.toString();
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String getRecipe(@PathVariable(value = "id") final String id) {

		log.info("get recipe by id " + id);

		Recipe recipe = repository.findOne(id);

		String response = "";
		if (recipe != null) {
			response = recipe.toString();
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getListRecipe(@RequestParam(name = "name", required = false) final String name,
			@RequestParam(name = "description", required = false) final String description) {

		Iterable<Recipe> recipes = null;
		if (isQueryParameterEmpty(name) && isQueryParameterEmpty(description)) {
			recipes = repository.findAll();
		} else if (!isQueryParameterEmpty(name) && isQueryParameterEmpty(description)) {
			recipes = repository.findByName(name);
		} else if (isQueryParameterEmpty(name) && !isQueryParameterEmpty(description)) {
			recipes = repository.findByDescription(description);
		} else if (!isQueryParameterEmpty(name) && !isQueryParameterEmpty(description)) {
			recipes = repository.findByNameAndDescription(name, description);
		}

		// recipes to JsonArray
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Recipe recipe = null;
		Iterator<Recipe> recipeIter = recipes.iterator();
		while (recipeIter.hasNext()) {
			recipe = recipeIter.next();
			sb.append(recipe.toString());
			if (recipeIter.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Update recipe.
	 * 
	 * Caution. All fields of the DTO must be sent. Missing fields will be
	 * interpreted as null.
	 * 
	 * @param id
	 *            the recipe ID
	 * @param recipe
	 *            the recipe
	 * @return response
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public String updateRecipe(@PathVariable(value = "id") final String id, @RequestBody Recipe recipe) {

		log.debug("start updating recipe");

		// get recipe
		Recipe receivedRecipe = repository.findOne(id);
		if (receivedRecipe == null) {
			return "didn't found recipe for id: " + id;
		}

		// update recipe
		receivedRecipe.setName(recipe.getName());
		receivedRecipe.setDescription(recipe.getDescription());
		Recipe updatedRecipe = repository.save(receivedRecipe);

		return updatedRecipe.toString();
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public String deleteRecipeByID(@PathVariable(value = "id") final String id) {

		log.info("delete recipe by id " + id);

		repository.delete(id);

		return "ok";
	}

	/**
	 * Validate if the <code>queryParamter</code> has been specified.
	 * 
	 * @param queryParamter
	 *            the query parameter
	 * @return true if the query paramter has a value, otherwise false
	 */
	private Boolean isQueryParameterEmpty(final String queryParamter) {
		if (queryParamter == null || queryParamter.length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

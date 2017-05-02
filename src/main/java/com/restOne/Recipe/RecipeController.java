package com.restOne.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {

		log.info("start creating recipe");

		Recipe createdRecipe = repository.save(recipe);

		return new ResponseEntity<Recipe>(createdRecipe, HttpStatus.CREATED);
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
	public ResponseEntity<List<Recipe>> getListRecipe(
			@RequestHeader(name = HttpHeaders.RANGE, required = false) final String headerValueRange,
			@RequestParam(name = "name", required = false) final String name,
			@RequestParam(name = "description", required = false) final String description) {

		Pageable pageable = null;
		if (headerValueRange != null && headerValueRange.length() > 0) {
			int page, size;

			// expect e.g item=1-20
			String value = headerValueRange;
			value = value.replace("item=", "");
			String[] ranges = value.split("-");
			page = Integer.valueOf(ranges[0]);
			size = Integer.valueOf(ranges[1]);

			pageable = new PageRequest(page, size);
		}

		Iterable<Recipe> recipes = null;
		Page<Recipe> page = null;
		if (isQueryParameterEmpty(name) && isQueryParameterEmpty(description)) {
			if (pageable != null) {
				page = repository.findAll(pageable);
				recipes = page.getContent();
			} else {
				recipes = repository.findAll();
			}
		} else if (!isQueryParameterEmpty(name) && isQueryParameterEmpty(description)) {
			if (pageable != null) {
				page = repository.findByName(name, pageable);
				recipes = page.getContent();
			} else {
				recipes = repository.findByName(name);
			}
		} else if (isQueryParameterEmpty(name) && !isQueryParameterEmpty(description)) {
			if (pageable != null) {
				page = repository.findByDescription(description, pageable);
				recipes = page.getContent();
			} else {
				recipes = repository.findByDescription(description);
			}
		} else if (!isQueryParameterEmpty(name) && !isQueryParameterEmpty(description)) {
			if (pageable != null) {
				page = repository.findByNameAndDescription(name, description, pageable);
				recipes = page.getContent();
			} else {
				recipes = repository.findByNameAndDescription(name, description);
			}
		}

		// Iterator to List
		List<Recipe> recipeList = new ArrayList<Recipe>();
		Iterator<Recipe> recipeIter = recipes.iterator();
		while (recipeIter.hasNext()) {
			recipeList.add(recipeIter.next());
		}

		// build response
		if (page != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_RANGE,
					page.getNumber() + "-" + page.getSize() + "/" + page.getTotalElements());
			return new ResponseEntity<List<Recipe>>(recipeList, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Recipe>>(recipeList, HttpStatus.OK);
		}
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
	 * @return true if the query parameter has a value, otherwise false
	 */
	private Boolean isQueryParameterEmpty(final String queryParamter) {
		if (queryParamter == null || queryParamter.length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

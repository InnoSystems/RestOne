package com.restOne.Recipe.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.restOne.Recipe.Recipe;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecipeControllerIT {

	@LocalServerPort
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/Recipes");
	}

	@After
	public void setDown() {
		deleteAllRecipes();
	}

	@Test
	public void createRecipeTest() throws Exception {

		// ### prepare test ###
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String name = "HeikePfanne";
		final String description = "Lecker...";

		HttpEntity<String> request = new HttpEntity<>(createRecipe(name, description), headers);

		// ### start test ###
		ResponseEntity<Recipe> response = template.postForEntity(base.toString(), request, Recipe.class);

		// ### validate ###
		Recipe recipe = response.getBody();
		assertNotNull(recipe.getId());
		assertThat(recipe.getName(), equalTo(name));
		assertThat(recipe.getDescription(), equalTo(description));
	}

	@Test
	public void getRecipeByIDTest() {

		// ### prepare test ###

		// create recipe
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String name = "HeikePfanne";
		final String description = "Lecker...";

		HttpEntity<String> request = new HttpEntity<>(createRecipe(name, description), headers);

		ResponseEntity<Recipe> response = template.postForEntity(base.toString(), request, Recipe.class);
		Recipe createdRecipe = response.getBody();

		// ### start test ###
		ResponseEntity<Recipe> secondResponse = template.getForEntity(base.toString() + "/" + createdRecipe.getId(),
				Recipe.class);

		// ### validate ###
		Recipe receivedRecipe = secondResponse.getBody();
		assertNotNull(receivedRecipe.getId());
		assertThat(receivedRecipe.getName(), equalTo(name));
		assertThat(receivedRecipe.getDescription(), equalTo(description));
	}

	@Test
	public void getRecipeList() {

		// ### prepare test ###

		// create recipe
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String name1 = "Kuchen123", name2 = "Kuchen123";
		final String description1 = "supi", description2 = "Gut";

		HttpEntity<String> requestOne = new HttpEntity<>(createRecipe(name1, description1), headers);
		HttpEntity<String> requestTwo = new HttpEntity<>(createRecipe(name2, description2), headers);

		template.postForEntity(base.toString(), requestTwo, Recipe.class);
		ResponseEntity<Recipe> createResponse = template.postForEntity(base.toString(), requestOne, Recipe.class);
		Recipe createdRecipe = createResponse.getBody();

		// ### start test ###
		ResponseEntity<Recipe[]> secondResponse = template
				.getForEntity(base.toString() + "?description=" + description1, Recipe[].class);

		ResponseEntity<Recipe[]> thirdResponse = template.getForEntity(base.toString() + "?name=" + name1,
				Recipe[].class);

		ResponseEntity<Recipe[]> fourthResponse = template
				.getForEntity(base.toString() + "?name=" + name2 + "&description=" + description2, Recipe[].class);

		// ### validate ###
		Recipe[] secondRecipe = secondResponse.getBody();
		assertEquals(createdRecipe.getId(), secondRecipe[0].getId());

		Recipe[] thirdRecipe = thirdResponse.getBody();
		assertEquals(2, thirdRecipe.length);

		Recipe[] fourthRecipe = fourthResponse.getBody();
		assertEquals(1, fourthRecipe.length);

	}

//	@Test
//	public void getRecipeListFindAllPagination() {
//
//		// ### prepare test ###
//
//		final String expectedFrom = "1", expectedTo = "5", expectedCount = "5";
//		final String name1 = "Kuchen123", name2 = "Kuchen123";
//		final String description1 = "supi", description2 = "Gut";
//
//		// set Content-Type in header
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//
//		// create 10 recipes
//		HttpEntity<String> requestOne = new HttpEntity<>(createRecipe("1", description1), headers);
//		HttpEntity<String> requestTwo = new HttpEntity<>(createRecipe("2", description2), headers);
//		HttpEntity<String> requestThree = new HttpEntity<>(createRecipe("3", description2), headers);
//		HttpEntity<String> requestFour = new HttpEntity<>(createRecipe("4", description2), headers);
//		HttpEntity<String> requestFive = new HttpEntity<>(createRecipe("5", description2), headers);
//		HttpEntity<String> requestSix = new HttpEntity<>(createRecipe("6", description1), headers);
//		HttpEntity<String> requestSeven = new HttpEntity<>(createRecipe("7", description2), headers);
//		HttpEntity<String> requestEight = new HttpEntity<>(createRecipe("8", description2), headers);
//		HttpEntity<String> requestNine = new HttpEntity<>(createRecipe("9", description2), headers);
//		HttpEntity<String> requestTen = new HttpEntity<>(createRecipe("10", description2), headers);
//		
//		template.postForEntity(base.toString(), requestOne, Recipe.class);
//		template.postForEntity(base.toString(), requestTwo, Recipe.class);
//		template.postForEntity(base.toString(), requestThree, Recipe.class);
//		template.postForEntity(base.toString(), requestFour, Recipe.class);
//		template.postForEntity(base.toString(), requestFive, Recipe.class);
//		template.postForEntity(base.toString(), requestSix, Recipe.class);
//		template.postForEntity(base.toString(), requestSeven, Recipe.class);
//		template.postForEntity(base.toString(), requestEight, Recipe.class);
//		template.postForEntity(base.toString(), requestNine, Recipe.class);
//		template.postForEntity(base.toString(), requestTen, Recipe.class);
//		
//		// ### start test ###
//		
//		// set range header
//		HttpHeaders reqHeaders = new HttpHeaders();
//		reqHeaders.set("Range", "item=" + expectedFrom + "-" + expectedTo);
//		
//		HttpEntity<String> entity = new HttpEntity<String>(reqHeaders);
//		ResponseEntity<Recipe[]> exchange = template.exchange(base.toString(), HttpMethod.GET, entity, Recipe[].class);
//
//		// ### validate ###
//		assertEquals(exchange.getStatusCode(), HttpStatus.OK);
//		HttpHeaders responseHeaders = exchange.getHeaders();
//		String contentRange = responseHeaders.get(HttpHeaders.CONTENT_RANGE).get(0);
//
//		assertEquals(expectedFrom + "-" + expectedTo + "/" + expectedCount, contentRange);
//	}

//	@Test
//	public void getRecipeListFindByNamePagination() {
//
//		// ### prepare test ###
//		final String from = "1", to = "2", count = "4";
//		final String name1 = "Kuchen123", name2 = "Suppe789";
//		final String description1 = "supi", description2 = "Gut";
//
//		// set Content-Type in header
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//
//		// create recipes
//		HttpEntity<String> requestOne = new HttpEntity<>(createRecipe(name1, description1), headers);
//		HttpEntity<String> requestTwo = new HttpEntity<>(createRecipe(name1, description2), headers);
//		HttpEntity<String> requestThree = new HttpEntity<>(createRecipe(name1, description2), headers);
//		HttpEntity<String> requestFour = new HttpEntity<>(createRecipe(name1, description2), headers);
//		HttpEntity<String> requestFive = new HttpEntity<>(createRecipe(name2, description2), headers);
//
//		template.postForEntity(base.toString(), requestOne, Recipe.class);
//		template.postForEntity(base.toString(), requestTwo, Recipe.class);
//		template.postForEntity(base.toString(), requestThree, Recipe.class);
//		template.postForEntity(base.toString(), requestFour, Recipe.class);
//		template.postForEntity(base.toString(), requestFive, Recipe.class);
//
//		// ### start test ###
//
//		// set range header
//		HttpHeaders reqHeaders = new HttpHeaders();
//		reqHeaders.set("Range", "item=" + from + "-" + to);
//
//		// start test
//		HttpEntity<String> entity = new HttpEntity<String>(reqHeaders);
//		ResponseEntity<Recipe[]> exchange = template.exchange(base.toString() + "?name=" + name1, HttpMethod.GET,
//				entity, Recipe[].class);
//
//		// ### validate ###
//		assertEquals(exchange.getStatusCode(), HttpStatus.OK);
//		HttpHeaders responseHeaders = exchange.getHeaders();
//		String contentRange = responseHeaders.get(HttpHeaders.CONTENT_RANGE).get(0);
//
//		assertEquals(from + "-" + to + "/" + count, contentRange);
//	}

	@Test
	public void updateRecipe() {

		// ### prepare test ###

		// create recipe
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String name = "HeikePfanne";
		final String description = "Lecker...";

		HttpEntity<String> createRequest = new HttpEntity<>(createRecipe(name, description), headers);

		ResponseEntity<Recipe> response = template.postForEntity(base.toString(), createRequest, Recipe.class);
		Recipe createdRecipe = response.getBody();

		// update recipe
		final String newName = "LisaPfanne";
		final String newDescription = "Geht so...";

		HttpEntity<String> updateRequest = new HttpEntity<>(createRecipe(newName, newDescription), headers);

		// ### start test ###
		template.put(base.toString() + "/" + createdRecipe.getId(), updateRequest);

		// ### validate ###
		ResponseEntity<Recipe> getRecipe = template.getForEntity(base.toString() + "/" + createdRecipe.getId(),
				Recipe.class);
		Recipe updatedRecipe = getRecipe.getBody();
		assertEquals(newName, updatedRecipe.getName());
		assertEquals(newDescription, updatedRecipe.getDescription());
	}

	@Test
	public void deleteRecipeTest() {

		// ### prepare test ###
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(createRecipe(), header);

		ResponseEntity<Recipe> response = template.postForEntity(base.toString(), request, Recipe.class);
		Recipe recipe = response.getBody();

		// ### start test ###
		template.delete(base.toString() + "/" + recipe.getId());

		// ### validate ###
		ResponseEntity<Recipe> getResponse = template.getForEntity(base.toString() + "/" + recipe.getId(),
				Recipe.class);
		Recipe emptyRecipe = getResponse.getBody();
		assertNull(emptyRecipe);
	}

	/**
	 * Delete all Recipes in the database.
	 */
	private void deleteAllRecipes() {

		ResponseEntity<Recipe[]> getResponse = template.getForEntity(base.toString(), Recipe[].class);
		Recipe[] recipes = getResponse.getBody();

		Recipe recipe = null;
		for (int i = 0; i < recipes.length; i++) {
			recipe = recipes[i];
			template.delete(base.toString() + "/" + recipe.getId());
		}

	}

	private String createRecipe() {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"name\":\"kuchen\",");
		sb.append("\"description\":\"lecker\"");
		sb.append("}");

		return sb.toString();
	}

	private String createRecipe(final String name, final String description) {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"name\":\"" + name + "\",");
		sb.append("\"description\":\"" + description + "\"");
		sb.append("}");

		return sb.toString();
	}

}

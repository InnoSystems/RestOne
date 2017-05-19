package com.restOne.Recipe.test;

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
public class RecipeControllerPagingIT {

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
	public void getRecipeListFindAllPagination1() {

		// ### prepare test ###
		final String from = "0", to = "5";
		final int expectedCount = 5, expectedMaxCount = 10;
		
		fillDatabaseWithData();
		
		// ### start test ###
		
		// set range header
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.set("Range", "item=" + from + "-" + to);
		
		HttpEntity<String> entity = new HttpEntity<String>(reqHeaders);
		ResponseEntity<Recipe[]> exchange = template.exchange(base.toString(), HttpMethod.GET, entity, Recipe[].class);

		// ### validate ###
		HttpHeaders responseHeaders = exchange.getHeaders();
		String contentRange = responseHeaders.get(HttpHeaders.CONTENT_RANGE).get(0);
		assertEquals(exchange.getStatusCode(), HttpStatus.OK);
		assertEquals(from + "-" + to + "/" + expectedMaxCount, contentRange);
		
		Recipe[] recipes = exchange.getBody();
		assertEquals("name1", recipes[0].getName());
		assertEquals("name5", recipes[4].getName());
		assertEquals(expectedCount, recipes.length);
	}
	
	@Test
	public void getRecipeListFindAllPagination2() {

		// ### prepare test ###
		final String from = "3", to = "7";
		final int expectedCount = 4, expectedMaxCount = 10;
		
		fillDatabaseWithData();
		
		// ### start test ###
		
		// set range header
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.set("Range", "item=" + from + "-" + to);
		
		HttpEntity<String> entity = new HttpEntity<String>(reqHeaders);
		ResponseEntity<Recipe[]> exchange = template.exchange(base.toString(), HttpMethod.GET, entity, Recipe[].class);

		// ### validate ###
		HttpHeaders responseHeaders = exchange.getHeaders();
		String contentRange = responseHeaders.get(HttpHeaders.CONTENT_RANGE).get(0);
		assertEquals(exchange.getStatusCode(), HttpStatus.OK);
		assertEquals(from + "-" + to + "/" + expectedMaxCount, contentRange);
		
		Recipe[] recipes = exchange.getBody();
		assertEquals("name4", recipes[0].getName());
		assertEquals("name7", recipes[3].getName());
		assertEquals(expectedCount, recipes.length);
	}
	
	@Test
	public void getRecipeListFindAllPagination3() {

		// ### prepare test ###
		final String from = "9", to = "100";
		final int expectedCount = 1, expectedMaxCount = 10;
		
		fillDatabaseWithData();
		
		// ### start test ###
		
		// set range header
		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.set("Range", "item=" + from + "-" + to);
		
		HttpEntity<String> entity = new HttpEntity<String>(reqHeaders);
		ResponseEntity<Recipe[]> exchange = template.exchange(base.toString(), HttpMethod.GET, entity, Recipe[].class);

		// ### validate ###
		HttpHeaders responseHeaders = exchange.getHeaders();
		String contentRange = responseHeaders.get(HttpHeaders.CONTENT_RANGE).get(0);
		assertEquals(exchange.getStatusCode(), HttpStatus.OK);
		assertEquals(from + "-" + to + "/" + expectedMaxCount, contentRange);
		
		Recipe[] recipes = exchange.getBody();
		assertEquals("name10", recipes[0].getName());
		assertEquals(expectedCount, recipes.length);
	}
	
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
	
	/**
	 * 	Create ten Recipes in the database.
	 */
	private void fillDatabaseWithData() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// create 10 recipes
		HttpEntity<String> requestOne = new HttpEntity<>(createRecipe("name1", "description1"), headers);
		HttpEntity<String> requestTwo = new HttpEntity<>(createRecipe("name2", "description2"), headers);
		HttpEntity<String> requestThree = new HttpEntity<>(createRecipe("name3", "description3"), headers);
		HttpEntity<String> requestFour = new HttpEntity<>(createRecipe("name4", "description4"), headers);
		HttpEntity<String> requestFive = new HttpEntity<>(createRecipe("name5", "description5"), headers);
		HttpEntity<String> requestSix = new HttpEntity<>(createRecipe("name6", "description6"), headers);
		HttpEntity<String> requestSeven = new HttpEntity<>(createRecipe("name7", "description7"), headers);
		HttpEntity<String> requestEight = new HttpEntity<>(createRecipe("name8", "description8"), headers);
		HttpEntity<String> requestNine = new HttpEntity<>(createRecipe("name9", "description9"), headers);
		HttpEntity<String> requestTen = new HttpEntity<>(createRecipe("name10", "description10"), headers);
		
		template.postForEntity(base.toString(), requestOne, Recipe.class);
		template.postForEntity(base.toString(), requestTwo, Recipe.class);
		template.postForEntity(base.toString(), requestThree, Recipe.class);
		template.postForEntity(base.toString(), requestFour, Recipe.class);
		template.postForEntity(base.toString(), requestFive, Recipe.class);
		template.postForEntity(base.toString(), requestSix, Recipe.class);
		template.postForEntity(base.toString(), requestSeven, Recipe.class);
		template.postForEntity(base.toString(), requestEight, Recipe.class);
		template.postForEntity(base.toString(), requestNine, Recipe.class);
		template.postForEntity(base.toString(), requestTen, Recipe.class);
		
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

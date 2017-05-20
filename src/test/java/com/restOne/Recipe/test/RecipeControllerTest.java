package com.restOne.Recipe.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.restOne.Recipe.test.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

	@Autowired
	private MockMvc mvc;

	/** Test Utils Class. */
	private TestUtils testUtils;

	@Before
	public void setUp() throws Exception {
		testUtils = new TestUtils();
	}

	@Test
	public void createRecipeTest() throws Exception {

		// ### prepare test ###
		final String name = "blaName";
		final String description = "blaDesc";

		final String recipe = testUtils.createRecipe(name, description);

		// ### start test ###
		ResultActions resultActions = mvc.perform(
				MockMvcRequestBuilders.post("/Recipes").content(recipe).contentType(MediaType.APPLICATION_JSON));

		// ### validate ###
		resultActions.andExpect(status().isCreated());
		resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
		resultActions.andExpect(jsonPath("$.name").value(name));
		resultActions.andExpect(jsonPath("$.description").value(description));

	}

}

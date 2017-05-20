package com.restOne.Recipe.test.utils;

/**
 * @author Adrian
 * 
 *         Test Utils Class.
 */
public class TestUtils {

	/**
	 * Create Recipe with standard values.
	 * 
	 * @return Recipe
	 */
	public String createRecipe() {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"name\":\"kuchen\",");
		sb.append("\"description\":\"lecker\"");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Create recipe with custom data.
	 * 
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 * @return Recipe
	 */
	public String createRecipe(final String name, final String description) {

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"name\":\"" + name + "\",");
		sb.append("\"description\":\"" + description + "\"");
		sb.append("}");

		return sb.toString();
	}
}

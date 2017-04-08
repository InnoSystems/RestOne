package com.restOne.Recipe;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
	
	List<Recipe> findByName(String name);
	
	List<Recipe> findByDescription(String description);
	
	List<Recipe> findByNameOrDescription(String name, String description);
}

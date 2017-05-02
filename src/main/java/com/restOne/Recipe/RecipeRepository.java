package com.restOne.Recipe;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

	Page<Recipe> findAll(Pageable pageable);

	List<Recipe> findByName(String name);

	Page<Recipe> findByName(String name, Pageable pageable);

	List<Recipe> findByDescription(String description);

	Page<Recipe> findByDescription(String description, Pageable pageable);

	List<Recipe> findByNameAndDescription(String name, String description);

	Page<Recipe> findByNameAndDescription(String name, String description, Pageable pageable);

	
}

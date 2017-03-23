package hello;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
	
	List<Recipe> findByName(String name);
	
	List<Recipe> findByDescription(String description);
	
	List<Recipe> findByNameAndDescription(String name, String description);
}

package hello;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, RecipeRepository repository) {

//		testDB(repository);

		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

	private void testDB(RecipeRepository repository) {
		
		// save a couple of customers
		repository.save(new Recipe("Jack", "Bauer"));
		repository.save(new Recipe("Chloe", "O'Brian"));
		repository.save(new Recipe("Kim", "Bauer"));
		repository.save(new Recipe("David", "Palmer"));
		repository.save(new Recipe("Michelle", "Dessler"));

		// fetch all customers
		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		String recipeId = null;
		for (Recipe customer : repository.findAll()) {
			log.info(customer.toString());
			recipeId = customer.getId();
		}
		log.info("");

		// fetch an individual customer by ID
		Recipe customer = repository.findOne(recipeId);
		log.info("Customer found with findOne(1L):");
		log.info("--------------------------------");
		log.info(customer.toString());
		log.info("");

		// fetch customers by last name
		log.info("Customer found with findByLastName('Bauer'):");
		log.info("--------------------------------------------");
		for (Recipe bauer : repository.findByName("Bauer")) {
			log.info(bauer.toString());
		}
		log.info("");
	}

}

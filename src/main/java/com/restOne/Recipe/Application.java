package com.restOne.Recipe;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.restOne.configurationExample.AnnotationConfiguration;
import com.restOne.configurationExample.TypeSafeConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentScan(basePackages = { "com.restOne.Recipe", "com.restOne.greeting", "com.restOne.configurationExample" })
@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
    	final ApplicationContext ctx = SpringApplication.run(Application.class, args);
    	final TypeSafeConfiguration typeSafeConfiguration = ctx.getBean(TypeSafeConfiguration.class);
        final AnnotationConfiguration annotationConfiguration = ctx.getBean(AnnotationConfiguration.class);

        info("Application initialized with the following configuration:");
        info(typeSafeConfiguration.toString());
        info(annotationConfiguration.toString());
        System.out.println();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, RecipeRepository repository) {

	return args -> {

	    System.out.println("Let's inspect the beans provided by Spring Boot:");

	    String[] beanNames = ctx.getBeanDefinitionNames();
	    Arrays.sort(beanNames);
	    for (String beanName : beanNames) {
		System.out.println(beanName);
	    }

	    LOG.debug("inspected the beans");

	};
    }
    
    private static void info(String text) {
        System.out.println(String.format("Application.java: %s", text));
    }

}

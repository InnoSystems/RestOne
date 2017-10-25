package com.restOne;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.restOne.Recipe.RecipeRepository;
import com.restOne.configurationExample.AnnotationConfiguration;
import com.restOne.configurationExample.TypeSafeConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guitar Model Object.
 * <P>
 * Various attributes of guitars, and related behaviour.
 * <P>
 * Note that is used to model the price - not double or float. See for more
 * information.
 * <ul>
 * <li>the first item
 * <li>the second item
 * <li>the third item
 * </ul>
 * 
 * @author akaminski
 * @version 1.0.1
 */
@ComponentScan(basePackages = { "com.restOne.Recipe", "com.restOne.greeting", "com.restOne.configurationExample" })
@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * 
     * @param ctx
     *            the ApplicationContext
     * @param repository
     *            the RecipeRepository
     * @return CommandLineRunner
     */
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

    /**
     * Simple file logger First paragraph.
     * <p>
     * <ul>
     * <li>the first item
     * <li>the second item
     * <li>the third item
     * </ul>
     * <p>
     * Second paragraph.
     * 
     * @param text
     *            to log
     */
    private static void info(String text) {
	System.out.println(String.format("Application.java: %s", text));
    }

    /**
     * Entry main of the restOne project
     * <p>
     * Nothing else to say
     * 
     * @param args
     *            are forwarded to SpringApplication.run
     */
    public static void main(String[] args) {
	final ApplicationContext ctx = SpringApplication.run(Application.class, args);
	final TypeSafeConfiguration typeSafeConfiguration = ctx.getBean(TypeSafeConfiguration.class);
	final AnnotationConfiguration annotationConfiguration = ctx.getBean(AnnotationConfiguration.class);

	info("Application initialized with the following configuration:");
	info(typeSafeConfiguration.toString());
	info(annotationConfiguration.toString());
	System.out.println();
    }
}

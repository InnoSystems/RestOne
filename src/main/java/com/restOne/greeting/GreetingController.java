package com.restOne.greeting;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /** This service must be called from a web-server like Apache Tomcat 6 which is running on port 9090 */
    @CrossOrigin(origins = "http://localhost:9090")
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "Kakki") String name) {
	return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
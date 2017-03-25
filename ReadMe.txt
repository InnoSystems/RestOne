Build Project:
--------------

- Use cmd and go to the folder of the project and execute "mvn clean package".
- To skip the tests execute "mvn clean package -DskipTests".


Start the Project/Web-Service:
------------------------------

- Use cmd and go to the folder of the project and execute "java -jar target/Inno-0.0.1-SNAPSHOT.jar"
	- The project starts intern a H2-Database and Tomcat

	
Test Project/Web-Service with cURL:
-----------------------------------

- Start the project as in "Start the project/Web-Service" described. 
- Download cURL "https://curl.haxx.se/download.html" and go to the folder "...\cURL\I386\" with cmd.
- Create a Recipe with "curl -i -H "Content-Type: application/json" -X POST -d "{\"name\":\"Suppe\",\"description\":\"lecker\"}" http://localhost:8080/Recipes"
	- The formatting \" is only under windows necessary.
- Get all Recipes with "curl -i -X GET http://localhost:8080/Recipes"


Informations:
-------------

- Spring Boot has been used to build the project. The project combine "Spring Data JPA" and "starter web/Spring MVC". 
	- "Spring Data JPA" has been included with this example: 
		- https://spring.io/guides/gs/accessing-data-jpa/
	- The REST-Endpoints (Spring MVC) has been added with this example:
		- https://spring.io/guides/gs/rest-service/
		
		
	- Checklist:
		- Start project outside of eclipse. (done)
		- Implement CRUD-Operations in the Endpoints and the DB-Interface (JPA). (done)
			- The Endpoint implementation/design used the following Google-Guide "https://cloud.google.com/apis/design/"
		- Use a UUID for the field "id" in the Entity "Recipe". (done) 
		- The Endpoints should receive a DTO instead of a String-Body. (done)
		- Implement Unit-Tests (Integration-Test) for all Endpoints. (done)
		
		
	- Open points:
		- The Endpoint "getList" didn't work when both Query-Parameter are set. The RecipeRepository must be extended.
		https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-eight-adding-functionality-to-a-repository/ 	       
		- Using an extern H2-Database instead of the intern H2-Database
		- Build the project as a War and deploy it in a Tomcat. 
		
		
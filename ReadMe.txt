Start RestOne GUI:
--------------
- Eclipse: right-click Application.java -> RunAs -> Java Application
- Browser: http://localhost:8080/

	
Test Project/Web-Service with cURL:
-----------------------------------

- Start the project as in "Start the project/Web-Service" described. 
- Download cURL "https://curl.haxx.se/download.html" and go to the folder "...\cURL\I386\" with cmd.
- Create a Recipe with "curl -i -H "Content-Type: application/json" -X POST -d "{\"name\":\"Suppe\",\"description\":\"lecker\"}" http://localhost:8080/Recipes"
	- The formatting \" is only under windows necessary.
- Get all Recipes with "curl -i -X GET http://localhost:8080/Recipes"


		
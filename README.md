Run mvn package to prepare the project.
Use run.sh to invoke the main class with the console (fix permissions: chmod +x run.sh)
Alternatively, you may run the JAR (it's a Spring jar), so: java -jar product-scrap-0.0.1-SNAPSHOT.jar

The console application will return the web-page scrap JSON response in 2 ways:
1. Start Spring in a separate thread and then:
    - Chose 'Make HTTP call...' from the console to get the result to the console
    - Click the URL mentioned (http://localhost:8080/api/v1/currants)
    - Open a tool such as Postman and use the same URL
2. Print the response directly without using Spring

When Spring Boot is up and running check out Swagger at:
http://localhost:8080/swagger-ui.html

Cheers!
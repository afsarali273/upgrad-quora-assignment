# upgrad-quora-assignment
This is a sample demo Quora like application using Spring boot ,Postgresql , JWT authentication and Swagger integration.

**Installation Guide:**

 - Make sure Postgres DB running locally with below config:
    * `server.host=localhost` 
    * `server.port=5432`
    *  `database.name=quora`
    *  `database.user=postgres`
    *  `database.password=password`
    
 - You can also run postgres DB using docker-compose
    * Go to `docker` folder then run below in cmd/terminal
        - `docker-compose up -d`
   
**To Run the application:**
   - First build the application using `mvn clean install -DskipTests`
   - Then run the application
# MediaDatabase
This is a Java Spring Boot project using Hibernate and a MySql server. This app tracks different media types a user consumes.

### The Idea
I have been using a simple spreadsheet to track most media like movies, TV shows, and books I've read for a few years. My list of media has been growing and becoming cumbersome to update and navigate.  I wanted to create an app that has a better interface and can have more features.

### Implementation
Being a relatively simple database app, I decided to use frameworks I haven't used before. Using Spring Boot made this project exceptionally easier to implement. Spring Boot handles much of the project configuration.

Another new framework I hadn't used before was Java Hibernate. I've used ORM tools before, so it was pretty easy to get set up once I had the proper database/relationship configurations.

At first, I used plain HTML and CSS for website styling. Later I decided to use Bootstrap. Bootstrap made it easier to implement some functions like the navbar and table styling. I liked the way the login and registration page looked and left those alone.

### User Authentication
For user authentication, I followed a guide at [hellokoding](https://hellokoding.com/) for understanding and creating the needed classes.

### Configuration
This project requires an ```application.properties``` file containing connection details for a data source. I am using a local MySQL server for development and provided an application.properties file that looked like:
```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.datasource.url=jdbc:mysql://localhost:3306/media_database?serverTimezone=America/Chicago
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

### Setup

#### Database
A database is required for this app. A user with create, read, update, and delete permissions on a database named media_database or matching the spring.datasource.url field in your application.properties file is needed. Replace the spring.datasource.username and spring.datasource.password with the created user's credentials.

#### Application
Create the ```src/main/resources/application.properties``` file that includes the fields mentioned above. 

I am using Maven to manage the dependencies for this project, so it can be built using the command ```mvnw clean install``` to create the war file in the target folder. 

From here, the war file can be run using ```java -jar MediaDatabase.war``` in the target folder. 

For my deployment, I am using Tomcat and a MySQL server. The Tomcat and the MySQL server are hosted on a Linux box with the built-in manager GUI.

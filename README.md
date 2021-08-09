# MediaDatabase
This is a Java Spring Boot project using Hibernate and a MySql server. This app tracks different media types a user consumes.

### The Idea
I have been using a simple spreadsheet to track most of the media like movies, TV shows and books I've read for a few years. With the length of the list growing and becoming cumbersome to update and navigate especailly on mobile, I wanted to create an app that has a better interface while having the ability to add more features.

### Implementation
This being a relatively simple database app, I decided to use frameworks I haven't used before. Using Spring Boot made this project exceptionally easier to implement with much of the project configuration handled by the framework.

Another new framework I hadn't used before was Java Hibernate. I have used ORM tools before so it was pretty easy to get set up once I had the proper database/relationship configurations.

### User Authentication
For user authentication I followed a guide at [hellokoding](https://hellokoding.com/) for understanding and creating the needed classes.

### Configuration
This project requires an ```application.properties``` file containing connection details for a datasource. I am using a local MySQL server for development and provided an application.properties file that looked like:
```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.datasource.url=jdbc:mysql://localhost:3306/media_database?serverTimezone=America/Chicago
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

# emp-mgnt
Employee Management Api service in Spring Boot

# Api supported
* `/users` Used to get the employee salary information based on the input parameters

* `/upload` Used to upload the employee salary information as csv data

# swagger Api URL
* [swagger Api URL](http://localhost:8080/swagger-ui.html)

# How to run unit test
* run maven command `mvn test -Dgroups=unit`

# How to run acceptance test
* run maven command `mvn test -Dgroups=acceptance`

# How to create package
* run maven command `mvn package -DskipTests`

# How to run the application
* run maven command `mvn spring-boot:run`

# Database Config
* System using H2 data base, set the following config in application config to override
1) spring.datasource.url 
2) spring.datasource.username 
3) spring.datasource.password
4) spring.jpa.database-platform  

# Spring related Reference documents
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#howto-execute-flyway-database-migrations-on-startup)

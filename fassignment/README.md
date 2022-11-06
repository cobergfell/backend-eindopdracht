# Final assignment Bootcamp Full Stack Developer 2021-03 Backend
##‘The Visual Arts and Conservatorium blogging website’
###Christophe Obergfell
####November 2022

## Introduction
This [Spring Boot](https://spring.io/projects/spring-boot) project is the backend of the web app developed as final assignment
of the Full Stack Developer bootcamp taught at the Dutch graduate educational institute NOVI (Utrecht, the Netherlands.) This specific bootcamp started in March 2021. 
The app developed can be seen as a basic webblog application intended for students
of conservatoria and art school to exchange ideas, images, audio and other documents.


## Installation
There are several options to build and run Spring Boot applications while still developing the application:
(adapted from the book: Spring in Action , Fifth Edition, by Craig Walls, ©2019 by Manning Publications Co)
* Running the application directly in the IDE with either Spring Tool Suite or IntelliJ
IDEA
* Running the application from the command line using the Maven spring-boot:run
goal or Gradle bootRun task
* Using Maven or Gradle to produce an executable JAR file that can be run at the
command line or deployed in the cloud (for deployment on a Platform as a Service Cloud)
* Using Maven or Gradle to produce a WAR file that can be deployed to a traditional Java
application server (such as Tomcat, WebSphere, WebLogic, or any other traditional Java application server
* Using Maven or Gradle to produce a container image that can be deployed anywhere that
containers are supported, including Kubernetes-based environments (modern cloud platforms are increasingly based on Kubernetes)

The two first options are only applicable while still developing the application.
To deploy the application for production, only the third and fourth options can be 
considered.


### If still in development phase 
Note: this one option is what is needed for the NOVI final assessment)
  * Install PostgreSQL version 11 or higher, host = localhost, port number = 5432, 
    and create a new database named "abc" with the query CREATE DATABASE 'abc';
  * Install [Apache Maven](https://maven.apache.org/install.html)
  * Run the application directly in the IDE
  * Or alternatively Execute the mvn command line <em>spring-boot:run </em> which triggers the download of Apache Tomcat and initializes the startup of Tomcat


## Upload a first example
* First make sure to be in the right schema with the query: set schema 'public';
* In a text editor, edit the SQL query that is given in the resources directory
  [insert_demo_into_database.sql](src\main\resources\insert_demo_into_database.sql)
  by replacing 'D:\your\path' by your own path to the app
* Run the query in PostgreSQL query tool


#### Running the Code as a Stand-Alone JAR Packaged Application (command line or deployed in the cloud)
Attention: this option still need to be tested

Running the Application:
[comment]: <> (* mvn clean package spring-boot:repackage)
* java -jar target/fassignment-0.0.1-SNAPSHOT.jar

#### Running the Code as a Stand-Alone WAR Packaged Application
Attention: this option is given as indication on how it should be implemented
but is not tested yet and the war archive is not available yet

The following is based on [https://www.baeldung.com/spring-boot-run-maven-vs-executable-jar](https://www.baeldung.com/spring-boot-run-maven-vs-executable-jar)

First tell Java which class to run, either by configuring the plugin:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <configuration>
                <mainClass>com.novi.fassignment.FassignmentApplication.java</mainClass>
            </configuration>
        </execution>
    </executions>
</plugin>
```

or setting the start-class property:
```xml
<properties>
    <start-class>com.novi.fassignment.FassignmentApplication.java</start-class>
</properties>
```
Then run the war file with these two commands:

mvn clean package spring-boot:repackage
java -jar target/fassignment-0.0.1-SNAPSHOT.war
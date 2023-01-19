# Final assignment Bootcamp Full Stack Developer 2021-03 Backend
##‘The Visual Arts and Conservatorium blogging website’
###Christophe Obergfell
####January 2023

## Introduction
This [Spring Boot](https://spring.io/projects/spring-boot) project is the backend of the web app developed as final assignment
of the Full Stack Developer bootcamp taught at the Dutch graduate educational institute NOVI (Utrecht, the Netherlands.) This specific bootcamp started in March 2021. 
The app developed can be seen as a basic webblog application intended for students
of conservatoria and art school to exchange ideas, images, audio and other documents.

## Install the database management system PostgreSQL
The app needs to be coupled with a relational database. Among the different available database management systems, [PostgreSQL](https://www.postgresql.org) is the one selected for this app to handle the data that need to be stored.
PostgreSQL can be downloaded from [https://www.postgresql.org](https://www.postgresql.org/download/).
For the installation, chose version 11 or higher, host = localhost, port number = 5432,
and create a new database named "abc".

## Install the database graphical administration tool pgAdmin
Together with PostgreSQL, installing the PostgreSQL graphical administration tool [pgAdmin](https://www.pgadmin.org)
will allow getting insight into the database for inspection of the data.  pgAdmin can be downloaded from
[https://www.pgadmin.org](https://www.pgadmin.org/download) (choose the latest version).

## Install the build automation tool Apache Maven  (while app is still in development)
To manage the life cycle of the app (write source code, validate, compile, test, deploy), a 'build automation tool' is needed. For a Spring Boot application, two candidates are available: [Gradle](https://www.gradle.org) or
[Apache Maven](https://maven.apache.org). For the development of this app, the second build automation tool was used,
it can be downloaded from [https://maven.apache.org/install.html](https://maven.apache.org/install.html).

## Install the IDE (while still in development)
This application was developed using [IntelliJ](https://www.jetbrains.com/idea/) as IDE (Interactive Development Environment).
It is recommended to use the same IDE to run the app while still in development.
Note that alternative and free IDE's are available such as [Eclipse](https://www.eclipse.org/community/eclipse_newsletter/2018/february/springboot.php),
[Visual Studio Code](https://code.visualstudio.com/docs/java/java-spring-boot) or [Theia](https://theia-ide.org/).

It is also possible to run the app from the command line using the Maven command 'spring-boot:run
goal' (for more details, see for example: 'Spring in Action' , Fifth Edition, by Craig Walls, ©2019 by Manning Publications Co)

## Run the app
Once all above mentioned programs are installed, the app can be run by running
the code main class 'FassignmentApplication.java'. A demo should load automatically when running the main file, to be viewed from the frontend.

## Install the app in production phase
For information only , this is how to install the app in production phase (not tested yet)
* Using Maven or Gradle to produce an executable JAR file that can be run at the
  command line or deployed in the cloud (for deployment on a Platform as a Service Cloud)
* Using Maven or Gradle to produce a WAR file that can be deployed to a traditional Java
  application server (such as Tomcat, WebSphere, WebLogic, or any other traditional Java application server
* Using Maven or Gradle to produce a container image that can be deployed anywhere that
  containers are supported, including Kubernetes-based environments (modern cloud platforms are increasingly based on Kubernetes)


If a JAR archive has been generated, run the app as a stand-alone JAR Packaged Application (command line or deployed in the cloud)
*mvn clean package spring-boot:repackage* and 
*java -jar target/fassignment-0.0.1-SNAPSHOT.jar*
If a WAR archive has been generated,run the app as a stand-alone WAR Packaged Application
*mvn clean package spring-boot:repackage* and *java -jar target/fassignment-0.0.1-SNAPSHOT.war*

Attention, for generating JAR or WAR archives, first tell Java which class to run, either by configuring the plugin:

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
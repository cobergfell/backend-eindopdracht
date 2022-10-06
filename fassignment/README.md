# Eindopdracht Bootcamp Full Stack Developer 2021-03 Backend
##‘The Visual Arts and Conservatorium blogging website’
###Christophe Obergfell
####Oktober 2022

## Inleiding
Deze map bevat de Spring Boot Backend onderdeel van de eindopdracht Bootcamp Full Stack Developer, 
NOVI Hogeschool Utrecht, leergroep 2021-03 
Het project is opgezet met [spring-boot](https://spring.io/projects/spring-boot).

## Doel
Deze app is een blogging platform voor studenten van de conservatorium en beeldende kunst studenten die bedoeld om de onderlinge relatie tussen muziek en beeldende kunst te onderzoeken.
In essentie biedt de app de mogelijkheid om een verzameling van blogs of projecten over dit onderwerp te verzamelen. Een project hier is simpelweg een grafische werk dat wordt gestuurd, eventueel met bijlage en muziek stukken, bedoeld om een uitwisseling van gedachten op gang te zetten.




## De applicatie starten
Om de app de runen is voorlopig een IDE (Interactive Development Environment)
nodig (de deployment van de app valt buiten de scope van de opleiding)
Deze app is ontwikkeld met IntelliJ IDEA Educational Edition 2020. Een andere IDE
zoals Visual Studio Code zou in principe ook kunnen met de noodzakelijke extensie.
  
Mogelijke alternatieve in de toekomst:
* Running as a Packaged Application
* Using External Tomcat [follow this link to see how](https://dzone.com/articles/spring-boot-with-external-tomcat)

## Een eerste voorbeeld uploaden
Om sneller een indruk te krijgen van hoe de blog eruitziet kan een voorbeeld
in de database geüploadet worden. De app dient eerst gedraaid te worden zodat de 
database opgebouwd wordt. Vervolgens kan de database populated worden
door de volgende query te draaien in postgreSQL:
[insert_demo_into_database.sql](src\main\resources\insert_demo_into_database.sql)
en vervang 'D:\your\path' door je eigen pad naar de app.
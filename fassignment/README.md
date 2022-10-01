# Eindopdracht Bootcamp Full Stack Developer 2021-03 Backend
##‘The Visual Arts and Conservatorium blogging website’
###Christophe Obergfell
####Oktober 2022

## Inleiding
Deze map bevat de Spring Boot Backend onderdeel van de eindopdracht Bootcamp Full Stack Developer, 
NOVI Hogeschol Utrecht, leergroep 2021-03 
Het project is opgezet met [spring-boot](https://spring.io/projects/spring-boot).

## Doel
Deze app (backend met frontend gecombineerd) heeft tot doel om een gezamlijke communicatie platform te bieden voor
muziek studenten en visual arts studenten. 

In essentie biedt de app een verzameling van projecten; onder project wordt verstaan een blog die geïnitieerd wordt door een aangemelde gebruiker door de copy van een grafische werk te uploaden.
De blog wordt ‘Painting Music’ genoemd en is Engelstalig om het zo toegankelijk te maken voor buitenlandse studenten.

De app is verder beschreven in de frontend onderdeel.


## De applicatie starten
Om de app de runen is voorlopig een IDE (Interactive Development Environment)
nodig (de deployment van de app valt buiten de scope van de opleiding)
Deze app is ontwikkeld met IntelliJ IDEA Educational Edition 2020. Een andere IDE
zoals Visual Studio Code zou in principe ook kunnen met de noodzakkelijke extensie.
  
Mogelijke alternative in de toekomst:
* Running as a Packaged Application
* Using External Tomcat [follow this link to see how](https://dzone.com/articles/spring-boot-with-external-tomcat)

## Een eerste voorbeeld uploaden
Om sneller een indruk te krijgen van hoe de blog eruit ziet kan een voorbeeld
in de database geuploaded worden. De app dient eerst gedraaid te worden zodat de 
database opgebouwd wordt. vervolgens kan de database populated worden
door de volgende query te draaien in postgreSQL:
[insert_demo_into_database.sql](src\main\resources\insert_demo_into_database.sql)
en vervang 'D:\your\path' door je eigen pad naar de app.
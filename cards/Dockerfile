#Starts with base image containing Java runtime
FROM openjdk:11

#Information about who maintains the image
MAINTAINER pranav

#Add application jar to container
COPY target/cards-0.0.1-SNAPSHOT.jar  cards-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java", "-jar", "cards-0.0.1-SNAPSHOT.jar"]
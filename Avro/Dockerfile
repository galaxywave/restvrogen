FROM openjdk:8-jdk-alpine

RUN apk update && apk add -y maven
COPY . /project
RUN  cd /project && mvn package

#run the spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dblabla", "-jar","/project/target/Avro-1.0-SNAPSHOT.jar"]
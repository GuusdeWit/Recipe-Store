FROM maven:3.8.6-openjdk-18 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:18-alpine
COPY --from=build /usr/src/app/target/recipe-store-0.0.1-SNAPSHOT.jar /usr/app/recipe-store-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/recipe-store-0.0.1-SNAPSHOT.jar"]
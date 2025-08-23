FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /aiqfome
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk AS runtime
WORKDIR /aiqfome
COPY --from=build /aiqfome/target/*.jar aiqfome.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "aiqfome.jar"]
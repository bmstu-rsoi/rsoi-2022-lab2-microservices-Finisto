FROM openjdk:19-alpine3.14
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rating.jar
EXPOSE 8050
ENTRYPOINT ["java","-jar","./rating.jar"]
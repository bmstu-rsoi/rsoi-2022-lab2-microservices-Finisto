FROM openjdk:19-alpine3.14
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} library.jar
EXPOSE 8060
ENTRYPOINT ["java","-jar","./library.jar"]
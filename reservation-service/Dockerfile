FROM openjdk:19-alpine3.14
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} reservation.jar
EXPOSE 8070
ENTRYPOINT ["java","-jar","./reservation.jar"]
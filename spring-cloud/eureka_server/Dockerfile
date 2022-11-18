FROM openjdk:11
COPY build/libs/eureka_server-0.0.1-SNAPSHOT.jar ae-eureka.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "ae-eureka.jar"]
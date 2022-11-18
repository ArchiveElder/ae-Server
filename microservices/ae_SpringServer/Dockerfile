FROM openjdk:11
COPY build/libs/ae_SpringServer-0.0.1-SNAPSHOT.jar ae-spring.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "ae-spring.jar"]
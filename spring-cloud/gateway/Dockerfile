FROM openjdk:11
COPY build/libs/gateway-0.0.1-SNAPSHOT.jar ae-gateway.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ae-gateway.jar"]
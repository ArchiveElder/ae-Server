FROM openjdk:11
COPY build/libs/community-0.0.1-SNAPSHOT.jar ae-community.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ae-community.jar"]
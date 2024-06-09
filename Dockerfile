FROM openjdk:21-jdk

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY build/libs/identity-reconciliation-0.0.1-SNAPSHOT.jar identity-reconciliation.jar

ENTRYPOINT ["java", "-jar", "identity-reconciliation.jar"]
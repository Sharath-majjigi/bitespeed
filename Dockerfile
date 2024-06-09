FROM openjdk:21-jdk

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

# Copy the Gradle wrapper and configuration to the container
COPY gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY build.gradle $APP_HOME
COPY settings.gradle $APP_HOME

COPY src $APP_HOME/src

RUN chmod +x gradlew

# Run the Gradle build to create the JAR file
RUN ./gradlew build

# Copy the JAR file to the container
COPY build/libs/*.jar identity-reconciliation.jar

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "identity-reconciliation.jar"]

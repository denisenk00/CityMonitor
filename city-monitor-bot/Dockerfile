#
# Build stage
#
FROM maven:3.8.5-openjdk-18 AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean package
#
# Package stage
#
FROM openjdk:18-alpine
COPY --from=MAVEN_BUILD target/city-monitor-bot-0.0.1-SNAPSHOT.jar city-monitor-bot.jar
ENTRYPOINT ["java", "-jar", "/city-monitor-bot.jar"]

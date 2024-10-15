FROM eclipse-temurin:22-alpine
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
COPY . ./
# Use build arguments for JWT_SECRET_KEY
ARG JWT_SECRET_KEY
# Set the environment variable using the build argument
ENV JWT_SECRET_KEY=${JWT_SECRET_KEY}
# Ensure mvnw has LF line endings
RUN sed -i 's/\r$//' mvnw && \
    chmod +x mvnw && \
    ./mvnw clean install -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/kickoff-tournament-management-0.0.1-SNAPSHOT.jar"]

FROM eclipse-temurin:22-alpine
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
COPY . ./
ENV JWT_SECRET_KEY=key
# Ensure mvnw has LF line endings
RUN sed -i 's/\r$//' mvnw && \
    chmod +x mvnw && \
    ./mvnw clean install -DskipTests
EXPOSE 3000
ENTRYPOINT ["java","-jar","target/kickoff-tournament-management-0.0.1-SNAPSHOT.jar"]

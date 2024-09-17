# CS203-Kickoff

### Setup Instructions for Backend
1. git clone im sure you know how to do this
2. navigate to backend
3. `chmod +x mvnw`
4. `./mvnw spring-boot:run`
5. `curl http://localhost:8080`

### Setup Instructions for Frontend
1. Enjoy

### Current List of Dependencies
1. Spring Web
2. Spring DevTools
3. Lombok (added 180924)

### How to Update Dependencies
1. Go to the Maven Central Repository
2. Copy the <dependency> block
3. Paste it into pom.xml
4. `./mvnw clean install`
5. Check the logs to see if your dependency is there

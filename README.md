# CS203-Kickoff

## Project Overview
Kickoff is a community-led tournament management system for football in Singapore, connecting clubs and players through various features including tournament hosting, player recruitment, and club management.

## Project Structure
- `/backend`: Spring Boot backend
- `/frontend`: React/Vite frontend

## Setup Instructions

### Backend Setup
1. Navigate to the backend directory:
   ```
   cd backend
   ```
2. Make the Maven wrapper executable:
   ```
   chmod +x mvnw
   ```
3. Run the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```
4. Verify the backend is running:
   ```
   curl http://localhost:8080
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
   ```
   npm run dev
   ```
4. Open your browser and visit `http://localhost:5173` (or the port Vite specifies)

## Current Dependencies

### Backend
1. Spring Web
2. Spring DevTools
3. Lombok
4. Starter Data JPA
5. H2 Database
6. Spring Security
7. Hibernate Validator

### Frontend
1. React
2. Vite
3. TypeScript
4. Redux Toolkit
5. React Router
6. Axios
7. Tailwind CSS

## Updating Dependencies

### Backend
1. Go to the Maven Central Repository
2. Copy the <dependency> block
3. Paste it into pom.xml
4. Run `./mvnw clean install`

### Frontend
1. Run `npm install [package-name]` for production dependencies
2. Run `npm install -D [package-name]` for development dependencies

## Contributing
[Add contribution guidelines here]

## License
[Add license information here]

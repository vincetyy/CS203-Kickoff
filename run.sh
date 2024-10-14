#!/bin/bash

# Open a new terminal in VS Code for the users microservice
code --new-window -r . && gnome-terminal -- bash -c "cd backend/users && mvn clean install && ./mvnw spring-boot:run; exec bash"

# Open a new terminal in VS Code for the club microservice
code --new-window -r . && gnome-terminal -- bash -c "cd backend/club && mvn clean install && ./mvnw spring-boot:run; exec bash"

# Open a new terminal in VS Code for the app microservice
code --new-window -r . && gnome-terminal -- bash -c "cd backend/app && mvn clean install && ./mvnw spring-boot:run; exec bash"

# Open a new terminal in VS Code for the frontend
code --new-window -r . && gnome-terminal -- bash -c "cd frontend && npm run dev; exec bash"

# Wait for all terminals to finish
wait
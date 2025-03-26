#!/bin/bash

echo "Building the project with Maven..."
./mvnw clean package -DskipTests

# Verificar si el build de Maven fue exitoso
if [ $? -ne 0 ]; then
  echo "Error building the project with Maven"
  exit 1
fi

echo "Building and running Docker containers..."
docker-compose up --build

# Para ejecutar en modo detached (background), usa:
# docker-compose up --build -d


# docker-compose down
# docker volume rm account-service_h2-data
#!/usr/bin/env bash

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Detect container runtime and compose command
if command -v podman >/dev/null 2>&1; then
    CONTAINER_CMD="podman"

    if podman compose version >/dev/null 2>&1; then
        COMPOSE_CMD="podman compose"
    elif command -v podman-compose >/dev/null 2>&1; then
        COMPOSE_CMD="podman-compose"
    else
        echo "Podman Compose not found."
        exit 1
    fi

elif command -v docker >/dev/null 2>&1; then
    CONTAINER_CMD="docker"

    if docker compose version >/dev/null 2>&1; then
        COMPOSE_CMD="docker compose"
    elif command -v docker-compose >/dev/null 2>&1; then
        COMPOSE_CMD="docker-compose"
    else
        echo "Docker Compose not found."
        exit 1
    fi

else
    echo "Neither Docker nor Podman is installed."
    exit 1
fi

echo "Using:"
echo "  Container runtime: $CONTAINER_CMD"
echo "  Compose command:   $COMPOSE_CMD"

cd "$PROJECT_ROOT"

echo "Starting infrastructure services..."

$COMPOSE_CMD up -d

echo "Waiting for PostgreSQL..."

SECONDS=0

until $CONTAINER_CMD exec financial-transactions-postgres \
    pg_isready -U postgres -d financial-transactions-db >/dev/null 2>&1
do
    if [ "$SECONDS" -ge 30 ]; then
        echo "Timeout waiting for PostgreSQL."
        exit 1
    fi

    sleep 2
done

echo "PostgreSQL is ready."

echo "Waiting for Redis..."

SECONDS=0

until $CONTAINER_CMD exec financial-transactions-redis \
    redis-cli ping >/dev/null 2>&1
do
    if [ "$SECONDS" -ge 30 ]; then
        echo "Timeout waiting for Redis."
        exit 1
    fi

    sleep 2
done

echo "Redis is ready."

echo "Waiting for application..."

SECONDS=0

until curl -fs http://localhost:8080/actuator/health >/dev/null 2>&1
do
    if [ "$SECONDS" -ge 16 ]; then
        echo "Application failed to become healthy."
        echo "Stopping application container..."

        $CONTAINER_CMD stop financial-transactions-ms >/dev/null 2>&1 || true

        echo "Starting application locally..."

        exec ./mvnw spring-boot:run
    fi

    sleep 2
done

echo "Application is ready."
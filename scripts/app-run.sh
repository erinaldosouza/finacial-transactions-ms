#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "Starting infrastructure services..."

cd "$PROJECT_ROOT/docker"
podman-compose up -d

echo "Waiting for PostgreSQL..."

until podman exec financial-transactions-postgres \
    pg_isready -U postgres -d financial-transactions-db >/dev/null 2>&1
do
    sleep 2
done

echo "PostgreSQL is ready."

echo "Starting Spring Boot..."

cd "$PROJECT_ROOT"
./mvnw spring-boot:run
#!/bin/bash
# Protec-X Startup Script
# This script simplifies the deployment of Protec-X

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo "======================================"
echo "Protec-X - Deployment Script"
echo "======================================"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker is not installed. Please install Docker to continue."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "ERROR: Docker Compose is not installed. Please install Docker Compose to continue."
    exit 1
fi

# Create .env if it doesn't exist
if [ ! -f .env ]; then
    echo "Creating .env from .env.example..."
    cp .env.example .env
    echo "Please edit .env with your configuration if needed."
    echo ""
fi

# Menu
echo "Select deployment option:"
echo "1) Start application (build if needed)"
echo "2) Stop application"
echo "3) Restart application"
echo "4) View logs"
echo "5) Rebuild application"
echo "6) Clean and rebuild"
echo ""
read -p "Enter option (1-6): " option

case $option in
    1)
        echo "Starting Protec-X..."
        docker-compose up -d
        echo ""
        echo "Waiting for services to be ready..."
        sleep 5
        docker-compose ps
        echo ""
        echo "Application is starting. Access it at: http://localhost:8080/"
        echo "Admin: admin@example.com / admin123"
        echo "User: user@example.com / user123"
        ;;
    2)
        echo "Stopping Protec-X..."
        docker-compose down
        echo "Application stopped."
        ;;
    3)
        echo "Restarting Protec-X..."
        docker-compose restart
        echo "Application restarted."
        ;;
    4)
        echo "Showing logs (press Ctrl+C to exit)..."
        docker-compose logs -f
        ;;
    5)
        echo "Rebuilding application..."
        docker-compose build --no-cache
        echo "Build complete."
        ;;
    6)
        echo "Cleaning and rebuilding..."
        docker-compose down -v
        docker-compose build --no-cache
        echo "Clean build complete."
        ;;
    *)
        echo "Invalid option"
        exit 1
        ;;
esac

echo ""
echo "Done!"

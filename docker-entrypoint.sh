#!/bin/bash

set -e

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
    if mysql -h "${DB_HOST:-mysql}" -u "${DB_USER}" -p"${DB_PASSWORD}" -e "SELECT 1" >/dev/null 2>&1; then
        echo "MySQL is ready!"
        break
    fi
    attempt=$((attempt + 1))
    echo "Attempt $attempt/$max_attempts - MySQL not ready yet. Waiting..."
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "ERROR: MySQL failed to start in time"
    exit 1
fi

# Update database.properties with environment variables
echo "Configuring database connection..."
cat > "$CATALINA_HOME/webapps/ROOT/WEB-INF/classes/database.properties" << EOF
db.url=${DB_URL:-jdbc:mysql://mysql:3306/ai_fraud_db}
db.user=${DB_USER:-root}
db.password=${DB_PASSWORD:-}
EOF

echo "Database configuration complete"

# Execute the main command
exec "$@"

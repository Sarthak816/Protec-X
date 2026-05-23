#!/bin/bash

# Health check script for the Tomcat application
# Returns 0 if healthy, 1 if unhealthy

HEALTH_CHECK_URL="http://localhost:8080/"
TIMEOUT=5

# Check if Tomcat is responding
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" --max-time $TIMEOUT "$HEALTH_CHECK_URL" 2>/dev/null)

if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 302 ] || [ "$HTTP_CODE" -eq 307 ]; then
    exit 0
else
    exit 1
fi

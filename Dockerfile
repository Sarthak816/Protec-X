# Multi-stage build for optimized image
FROM maven:3.8.1-openjdk-11-slim as builder

WORKDIR /build

# Copy pom.xml and download dependencies
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:9.0-jre11-slim

# Set environment variables
ENV CATALINA_OPTS="-Xms512m -Xmx2048m"
ENV APP_ENV=production

# Create app user
RUN useradd -m -s /bin/bash appuser

# Copy WAR file from builder stage
COPY --from=builder /build/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copy database schema for initialization
COPY database/schema.sql /docker-entrypoint-initdb.d/

# Copy entrypoint script
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# Copy health check script
COPY healthcheck.sh /healthcheck.sh
RUN chmod +x /healthcheck.sh

# Expose port
EXPOSE 8080

# Set working directory
WORKDIR /usr/local/tomcat

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD /healthcheck.sh

# Switch to non-root user
USER appuser

# Use custom entrypoint
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]

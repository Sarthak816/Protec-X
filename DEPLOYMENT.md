# Protec-X Deployment Guide

This guide provides step-by-step instructions for deploying the Protec-X fraud detection system in various environments.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Quick Start with Docker](#quick-start-with-docker)
3. [Traditional Deployment (Tomcat + MySQL)](#traditional-deployment)
4. [Environment Configuration](#environment-configuration)
5. [Database Setup](#database-setup)
6. [Deployment Verification](#deployment-verification)
7. [Troubleshooting](#troubleshooting)
8. [Production Considerations](#production-considerations)

---

## Prerequisites

### For Docker Deployment (Recommended)
- Docker 20.10 or higher
- Docker Compose 1.29 or higher

### For Traditional Deployment
- Java Development Kit (JDK) 11 or higher
- Apache Tomcat 9.0 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher
- Git

---

## Quick Start with Docker

The easiest way to deploy Protec-X is using Docker and Docker Compose.

### Step 1: Clone the Repository

```bash
git clone https://github.com/Sarthak816/Protec-X.git
cd Protec-X
```

### Step 2: Configure Environment Variables

```bash
# Copy the example environment file
cp .env.example .env

# Edit the .env file with your settings (optional - defaults are secure)
nano .env
```

### Step 3: Build and Deploy

```bash
# Build the Docker image
docker-compose build

# Start the services
docker-compose up -d

# Verify services are running
docker-compose ps
```

### Step 4: Access the Application

Open your browser and navigate to:

```
http://localhost:8080/
```

### Step 5: Verify Deployment

Check if both services are healthy:

```bash
# Check MySQL health
docker-compose exec mysql mysqladmin ping -h localhost

# Check Tomcat health
curl http://localhost:8080/

# View logs
docker-compose logs -f web
docker-compose logs -f mysql
```

---

## Traditional Deployment

For environments without Docker support.

### Step 1: Install Dependencies

#### On Ubuntu/Debian:
```bash
# Install JDK 11
sudo apt-get update
sudo apt-get install -y openjdk-11-jdk

# Install Tomcat
sudo apt-get install -y tomcat9 tomcat9-docs tomcat9-examples

# Install MySQL
sudo apt-get install -y mysql-server

# Install Maven
sudo apt-get install -y maven
```

#### On macOS:
```bash
# Install using Homebrew
brew install openjdk@11
brew install tomcat
brew install mysql
brew install maven
```

### Step 2: Start MySQL Service

```bash
# Linux
sudo systemctl start mysql
sudo systemctl enable mysql

# macOS
brew services start mysql

# Windows
# Use Services application or MySQL Command Prompt
```

### Step 3: Configure Database

```bash
# Connect to MySQL
mysql -u root -p

# In MySQL shell, execute:
SOURCE database/schema.sql;
```

### Step 4: Configure Database Connection

Edit `backend/src/main/resources/database.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/ai_fraud_db
db.user=root
db.password=your_mysql_password
```

### Step 5: Build the Application

```bash
cd backend

# Clean and package
mvn clean package

# The WAR file will be created at:
# target/ai-fraud-detection-1.0-SNAPSHOT.war
```

### Step 6: Deploy to Tomcat

```bash
# Copy WAR file to Tomcat
cp backend/target/ai-fraud-detection-1.0-SNAPSHOT.war \
   /var/lib/tomcat9/webapps/

# Alternatively for standalone Tomcat:
cp backend/target/ai-fraud-detection-1.0-SNAPSHOT.war \
   $CATALINA_HOME/webapps/
```

### Step 7: Start Tomcat

```bash
# Linux
sudo systemctl start tomcat9
sudo systemctl enable tomcat9

# macOS
catalina start

# Check status
sudo systemctl status tomcat9

# View logs
tail -f /var/log/tomcat9/catalina.out
```

### Step 8: Verify Deployment

```bash
# Test application
curl http://localhost:8080/ai-fraud-detection-1.0-SNAPSHOT/

# Should return HTML login page
```

---

## Environment Configuration

### Configuration Methods (in order of precedence):

1. **Environment Variables** (highest priority)
   ```bash
   export DB_URL=jdbc:mysql://host:3306/database
   export DB_USER=username
   export DB_PASSWORD=password
   ```

2. **database.properties File** (medium priority)
   ```
   db.url=jdbc:mysql://localhost:3306/ai_fraud_db
   db.user=root
   db.password=your_password
   ```

3. **Defaults** (lowest priority)
   - Uses fallback values if neither above is set

### Available Configuration Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | JDBC connection URL | `jdbc:mysql://localhost:3306/ai_fraud_db` |
| `DB_USER` | Database username | `root` |
| `DB_PASSWORD` | Database password | (empty) |
| `CATALINA_OPTS` | Tomcat JVM options | `-Xms512m -Xmx2048m` |
| `APP_ENV` | Environment type | `production` |

---

## Database Setup

### Automatic Setup (Docker)

The database is automatically initialized using `database/schema.sql` when using Docker Compose.

### Manual Setup

```bash
# Connect to MySQL
mysql -u root -p

# Execute in MySQL shell:
CREATE DATABASE IF NOT EXISTS ai_fraud_db;
USE ai_fraud_db;

-- Create tables
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT,
    amount DECIMAL(12,2),
    timestamp DATETIME,
    status VARCHAR(20),
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transactionId BIGINT,
    alertMessage VARCHAR(500),
    createdAt DATETIME,
    FOREIGN KEY (transactionId) REFERENCES transactions(id) ON DELETE CASCADE
);

-- Insert sample users
INSERT INTO users (name, email, password, role) VALUES 
    ('Admin User', 'admin@example.com', 'admin123', 'ADMIN'),
    ('Test User', 'user@example.com', 'user123', 'USER');

-- Insert sample transactions
INSERT INTO transactions (userId, amount, timestamp, status) VALUES 
    (2, 50.00, NOW(), 'PENDING'),
    (2, 15000.00, NOW(), 'PENDING');
```

### Default Test Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@example.com | admin123 |
| User | user@example.com | user123 |

---

## Deployment Verification

### Health Checks

```bash
# Check application is running
curl -v http://localhost:8080/

# Check MySQL connectivity
mysql -h localhost -u root -p -e "SELECT 1"

# Check WAR deployment (Tomcat)
ls -la /var/lib/tomcat9/webapps/ | grep war
```

### Login Test

1. Navigate to `http://localhost:8080/`
2. Log in with:
   - **Email**: `admin@example.com`
   - **Password**: `admin123`
3. Verify admin dashboard loads
4. Log in as regular user:
   - **Email**: `user@example.com`
   - **Password**: `user123`
5. Verify user dashboard loads

### API Testing

```bash
# Test authentication endpoint
curl -X POST http://localhost:8080/api/auth \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'

# Test transaction endpoint
curl http://localhost:8080/api/transactions
```

---

## Troubleshooting

### Docker Issues

#### Containers won't start

```bash
# Check logs
docker-compose logs mysql
docker-compose logs web

# Rebuild images
docker-compose down
docker-compose build --no-cache
docker-compose up
```

#### Port already in use

```bash
# Find process using port
sudo lsof -i :8080
sudo lsof -i :3306

# Kill process or use different port in docker-compose.yml
```

### Traditional Deployment Issues

#### MySQL Connection Error

```bash
# Verify MySQL is running
sudo systemctl status mysql

# Check connection
mysql -h localhost -u root -p -e "SELECT 1"

# Verify credentials in database.properties
cat backend/src/main/resources/database.properties
```

#### Tomcat Not Starting

```bash
# Check Java is installed
java -version

# Verify Tomcat installation
$CATALINA_HOME/bin/version.sh

# Check Tomcat logs
tail -f $CATALINA_HOME/logs/catalina.out
```

#### 404 Errors

```bash
# Verify WAR is deployed
ls -la $CATALINA_HOME/webapps/

# Check Tomcat manager
curl http://localhost:8080/manager/html

# Verify context path
# Should be either:
# - http://localhost:8080/ai-fraud-detection-1.0-SNAPSHOT/
# - http://localhost:8080/ROOT/ (if renamed to ROOT.war)
```

#### Database Tables Don't Exist

```bash
# Verify schema was loaded
mysql -u root -p -e "USE ai_fraud_db; SHOW TABLES;"

# If empty, manually load schema
mysql -u root -p ai_fraud_db < database/schema.sql
```

---

## Production Considerations

### Security

- [ ] **Change default credentials** immediately in production
- [ ] **Use strong passwords** for database users
- [ ] **Enable SSL/HTTPS** for Tomcat
- [ ] **Configure firewall rules** to limit access
- [ ] **Use environment variables** for sensitive data (not in files)
- [ ] **Hash passwords** in database (not plaintext)
- [ ] **Implement rate limiting** on authentication endpoints
- [ ] **Enable audit logging** for all transactions

### Performance

- [ ] **Increase Tomcat heap memory**:
  ```bash
  export CATALINA_OPTS="-Xms1024m -Xmx4096m"
  ```

- [ ] **Configure connection pooling** in database.properties
- [ ] **Enable database indexing** for frequently queried columns
- [ ] **Implement caching** for fraud detection rules

### Monitoring & Logging

- [ ] **Set up centralized logging** (ELK stack, Splunk, etc.)
- [ ] **Monitor application performance** (APM tools)
- [ ] **Set up alerts** for errors and anomalies
- [ ] **Backup database** regularly
- [ ] **Monitor disk space** on database server

### Backup & Disaster Recovery

```bash
# Backup MySQL database
mysqldump -u root -p ai_fraud_db > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore from backup
mysql -u root -p ai_fraud_db < backup_20240523_120000.sql

# Docker volume backup
docker run --rm -v protec-x_mysql_data:/data -v $(pwd):/backup \
  ubuntu tar czf /backup/mysql_backup.tar.gz /data
```

### Scaling

- [ ] **Use load balancing** for multiple Tomcat instances
- [ ] **Implement database replication** for MySQL
- [ ] **Use container orchestration** (Kubernetes) for Docker deployments
- [ ] **Enable connection pooling** with proper pool size

---

## Support & Resources

- **Repository**: https://github.com/Sarthak816/Protec-X
- **Issues**: Report bugs via GitHub Issues
- **Documentation**: See README.md for architecture details

---

## Deployment Checklist

Before going live, ensure:

- [ ] Database is initialized and schema is loaded
- [ ] Database credentials are configured securely
- [ ] Application starts without errors
- [ ] Login functionality works with test credentials
- [ ] Dashboard loads and displays data
- [ ] Fraud detection rules are functioning
- [ ] Monitoring and logging are operational
- [ ] Backups are configured
- [ ] Security measures are in place
- [ ] Performance tests pass
- [ ] Documentation is updated
- [ ] Team is trained on deployment process

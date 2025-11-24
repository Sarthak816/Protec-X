
-- Create database and tables for AI Fraud Detection System
CREATE DATABASE IF NOT EXISTS ai_fraud_db;
USE ai_fraud_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(150) UNIQUE,
  password VARCHAR(100),
  role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS transactions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  userId BIGINT,
  amount DECIMAL(12,2),
  timestamp DATETIME,
  status VARCHAR(20),
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS alerts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  transactionId BIGINT,
  alertMessage VARCHAR(500),
  createdAt DATETIME,
  FOREIGN KEY (transactionId) REFERENCES transactions(id) ON DELETE CASCADE
);

-- Sample user
INSERT INTO users (name, email, password, role) VALUES ('Admin User','admin@example.com','admin123','ADMIN');
INSERT INTO users (name, email, password, role) VALUES ('Test User','user@example.com','user123','USER');

-- Sample transactions
INSERT INTO transactions (userId, amount, timestamp, status) VALUES (2, 50.00, NOW(), 'PENDING');
INSERT INTO transactions (userId, amount, timestamp, status) VALUES (2, 15000.00, NOW(), 'PENDING'); -- should be flagged by mock detector

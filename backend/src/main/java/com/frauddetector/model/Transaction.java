
package com.frauddetector.model;

import java.sql.Timestamp;

public class Transaction {
    private long id;
    private long userId;
    private double amount;
    private Timestamp timestamp;
    private String status; // e.g., PENDING, COMPLETED, FRAUD

    public Transaction() {}

    public Transaction(long id, long userId, double amount, Timestamp timestamp, String status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public java.sql.Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(java.sql.Timestamp timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

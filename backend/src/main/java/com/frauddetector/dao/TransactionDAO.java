
package com.frauddetector.dao;

import com.frauddetector.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getRecentPendingTransactions(int limit) throws SQLException {
        String sql = "SELECT id,userId,amount,timestamp,status FROM transactions WHERE status='PENDING' ORDER BY timestamp DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            List<Transaction> list = new ArrayList<>();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getLong("id"));
                t.setUserId(rs.getLong("userId"));
                t.setAmount(rs.getDouble("amount"));
                t.setTimestamp(rs.getTimestamp("timestamp"));
                t.setStatus(rs.getString("status"));
                list.add(t);
            }
            return list;
        }
    }

    public void markTransactionAsFraud(long transactionId, String reason) throws SQLException {
        String update = "UPDATE transactions SET status='FRAUD' WHERE id=?";
        String insertAlert = "INSERT INTO alerts (transactionId, alertMessage, createdAt) VALUES (?, ?, NOW())";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(update)) {
                ps.setLong(1, transactionId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement(insertAlert)) {
                ps2.setLong(1, transactionId);
                ps2.setString(2, reason);
                ps2.executeUpdate();
            }
            con.commit();
        }
    }

    public void saveTransaction(Transaction t) throws SQLException {
        String insert = "INSERT INTO transactions (userId, amount, timestamp, status) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, t.getUserId());
            ps.setDouble(2, t.getAmount());
            ps.setTimestamp(3, t.getTimestamp());
            ps.setString(4, t.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getLong(1));
            }
        }
    }
}

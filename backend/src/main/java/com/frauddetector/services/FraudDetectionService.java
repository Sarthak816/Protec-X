
package com.frauddetector.services;

import com.frauddetector.dao.TransactionDAO;
import com.frauddetector.model.Transaction;

import java.util.List;

public class FraudDetectionService {
    private TransactionDAO transactionDAO;
    private MLAnomalyDetector detector;

    public FraudDetectionService() {
        this.transactionDAO = new TransactionDAO();
        this.detector = new MLAnomalyDetector();
    }

    // Check recent transactions and mark fraud if anomaly score > threshold
    public void analyzeRecentTransactions() {
        try {
            List<Transaction> recent = transactionDAO.getRecentPendingTransactions(50);
            for (Transaction t : recent) {
                double score = detector.anomalyScore(t);
                if (score > 2.5) { // threshold (z-score like)
                    transactionDAO.markTransactionAsFraud(t.getId(), "High anomaly score: " + score);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

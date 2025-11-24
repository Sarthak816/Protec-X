
package com.frauddetector.services;

public class AlertService {
    // In production this might send emails, push notifications or integrate with a messaging system.
    public void sendAlert(long transactionId, String message) {
        System.out.println("ALERT for txn=" + transactionId + ": " + message);
    }
}

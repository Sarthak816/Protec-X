
package com.frauddetector.threads;

import com.frauddetector.services.FraudDetectionService;

public class RealTimeMonitorThread extends Thread {
    private volatile boolean running = true;
    private FraudDetectionService service = new FraudDetectionService();

    public RealTimeMonitorThread() {
        setName("RealTimeMonitorThread");
    }

    @Override
    public void run() {
        while (running) {
            try {
                service.analyzeRecentTransactions();
                // Sleep for a short interval before next monitoring cycle
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}

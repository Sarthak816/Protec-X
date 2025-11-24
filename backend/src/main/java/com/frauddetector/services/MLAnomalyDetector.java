
package com.frauddetector.services;

import com.frauddetector.model.Transaction;

/*
 A very small mock anomaly detector.
 In real projects you would replace this with a trained model or a library.
 For demonstration we compute a rudimentary score based on amount.
*/
public class MLAnomalyDetector {

    // Returns a mock anomaly score (higher means more anomalous)
    public double anomalyScore(Transaction t) {
        if (t == null) return 0.0;
        double amt = t.getAmount();
        // simple heuristic: extremely large or negative amounts are suspicious
        if (amt <= 0) return 5.0;
        if (amt > 10000) return amt / 2000.0;
        if (amt > 2000) return 3.0 + (amt - 2000)/2000.0;
        // else compute small score based on logarithm
        return Math.log(amt + 1) / 0.5;
    }
}

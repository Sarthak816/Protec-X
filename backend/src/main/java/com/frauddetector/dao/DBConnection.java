
package com.frauddetector.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(in);
            url = getConfigValue("DB_URL", props.getProperty("db.url", "jdbc:mysql://localhost:3306/ai_fraud_db"));
            user = getConfigValue("DB_USER", props.getProperty("db.user", "root"));
            password = getConfigValue("DB_PASSWORD", props.getProperty("db.password", ""));
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback - check environment variables first
            url = getConfigValue("DB_URL", "jdbc:mysql://localhost:3306/ai_fraud_db");
            user = getConfigValue("DB_USER", "root");
            password = getConfigValue("DB_PASSWORD", "");
        }
        
        System.out.println("Database URL: " + url);
        System.out.println("Database User: " + user);
    }

    /**
     * Get configuration value from environment variable or use default
     */
    private static String getConfigValue(String envKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        return envValue != null && !envValue.isEmpty() ? envValue : defaultValue;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}


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
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback - change as needed
            url = "jdbc:mysql://localhost:3306/ai_fraud_db";
            user = "root";
            password = "";
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

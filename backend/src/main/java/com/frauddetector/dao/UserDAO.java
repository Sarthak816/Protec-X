
package com.frauddetector.dao;

import com.frauddetector.model.User;
import java.sql.*;

public class UserDAO {
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id,name,email,password,role FROM users WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("role"));
                }
                return null;
            }
        }
    }
}

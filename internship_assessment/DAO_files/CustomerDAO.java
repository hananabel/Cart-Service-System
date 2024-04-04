// CustomerDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    // Method to check if a username already exists in the database
    public boolean usernameExists(String username) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM customer WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Return true if username exists, false otherwise
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false in case of any error
    }

    // Method to add a new customer to the database
    public void addCustomer(String username) {
        if (usernameExists(username)) {
            System.out.println(username + " account already exists.");
            return;
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO customer (username) VALUES (?)")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            //System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch customer ID by username
    public int fetchCustomerIdByUsername(String username) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT customerId FROM customer WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customerId");
                }
            }
        } catch (SQLException e) {
            // Handle the exception gracefully
            e.printStackTrace(); // Consider logging the error instead
        }
        return -1; // Return -1 if customer ID is not found or in case of any error
    }
}

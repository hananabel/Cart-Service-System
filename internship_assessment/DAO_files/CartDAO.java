
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {
    // Method to check if a cart already exists for a given customer ID
    // Returns true if cart exists, false otherwise
    public boolean cartExists(int customerId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM cart WHERE customerId = ?");
        ) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Return true if cart exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if cart doesn't exist or in case of any error
    }

    // Method to add a new cart for a given customer ID
    public void addCart(int customerId) {
        if (cartExists(customerId)) {
            System.out.println("Cart already exists for customer ID: " + customerId);
            return;
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cart (customerId) VALUES (?)")
        ) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            System.out.println("Cart added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

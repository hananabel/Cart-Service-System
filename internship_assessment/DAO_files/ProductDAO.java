import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {
    
    public void addProduct(String productName, double price) {
        if (productExists(productName)) {
            //System.out.println(productName + " already exists.");
            return;
        }
        
        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, productName);
            stmt.setDouble(2, price);
            
            stmt.executeUpdate();
            //System.out.println("Product added successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private boolean productExists(String productName) {
        String sql = "SELECT COUNT(*) FROM product WHERE name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    public String getProductNameById(int productId) {
        String productName = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM product WHERE productId = ?");
        ) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productName = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productName;
    }
    public double getProductPriceById(int productId) {
        double price = 0.0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT price FROM product WHERE productId = ?");
        ) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }
}

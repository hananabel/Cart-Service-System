
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO {

    // Method to add an item to the cart
    public void addItemToCart(int customerId, String productName, int quantity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cartitem (cartId, productId, quantity) VALUES (?, ?, ?)")
        ) {
            int cartId = getCartIdByCustomerId(customerId);
            int productId = getProductIdByName(productName);
            
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            
            stmt.executeUpdate();
            System.out.println("Item added to cart successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove an item from the cart
    public void removeItemFromCart(int customerId, String productName, int quantity) {
        try (Connection conn = DBConnection.getConnection()) {
            int cartId = getCartIdByCustomerId(customerId);
            int productId = getProductIdByName(productName);

            if (cartId != -1 && productId != -1) {
                int currentQuantity = getCurrentQuantityInCart(cartId, productId);
                
                if (currentQuantity <= quantity) {
                    // If the quantity to remove exceeds or equals the current quantity, remove the item from cart
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cartitem WHERE cartId = ? AND productId = ?")) {
                        stmt.setInt(1, cartId);
                        stmt.setInt(2, productId);
                        stmt.executeUpdate();
                        System.out.println("Item removed from cart successfully.");
                    }
                } else {
                    // Otherwise, update the quantity in the cart
                    try (PreparedStatement stmt = conn.prepareStatement("UPDATE cartitem SET quantity = quantity - ? WHERE cartId = ? AND productId = ?")) {
                        stmt.setInt(1, quantity);
                        stmt.setInt(2, cartId);
                        stmt.setInt(3, productId);
                        stmt.executeUpdate();
                        System.out.println("Item quantity updated in cart.");
                    }
                }
            } else {
                System.out.println("Customer or product not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch the cart ID based on the customer ID
    private int getCartIdByCustomerId(int customerId) {
        int cartId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT cartId FROM cart WHERE customerId = ?");
        ) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cartId = rs.getInt("cartId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartId;
    }

    // Method to fetch the product ID based on the product name
    private int getProductIdByName(String productName) {
        int productId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT productId FROM product WHERE name = ?");
        ) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    productId = rs.getInt("productId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productId;
    }

    private int getCurrentQuantityInCart(int cartId, int productId) {
        int currentQuantity = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT quantity FROM cartitem WHERE cartId = ? AND productId = ?");
        ) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentQuantity = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentQuantity;
    }

    public List<CartItem> getCartItemsByCustomerId(int customerId) {
        List<CartItem> cartItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cartitem WHERE cartId = ?");
        ) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //int cartItemId = rs.getInt("cartItemId");
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    cartItems.add(new CartItem(customerId, productId, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }
    public void clearCart(int customerId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cartitem WHERE cartId = ?");
        ) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            System.out.println("Cart cleared successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    


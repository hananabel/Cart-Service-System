
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the shopping website!");

        // Ask the user to log in or register
        System.out.println("Do you have an account? (yes/no)");
        String response = scanner.nextLine();

        CustomerDAO customerDAO = new CustomerDAO();

        if (response.equalsIgnoreCase("yes")) {
            // Log in
            System.out.println("Please enter your username:");
            String username = scanner.nextLine();

            // Check if the username exists
            if (!customerDAO.usernameExists(username)) {
                System.out.println("Username doesn't exist. Please create an account.");
                // Prompt the user to create an account
                createAccount(scanner, customerDAO, username);
            } else {
                System.out.println("Logged in successfully!");

                //CREATE CART
                int customerId = customerDAO.fetchCustomerIdByUsername(username);
                cart(username, customerDAO);

                //PRODUCTS
                product();

                // Add or remove items from the cart
                updateCart(scanner, customerId);
            }
        } else if (response.equalsIgnoreCase("no")) {
            // Create an account
            System.out.println("Please create an account.");
            createAccount(scanner, customerDAO, null);
        } else {
            System.out.println("Invalid response. Please try again.");
        }

        scanner.close();
    }

    private static void createAccount(Scanner scanner, CustomerDAO customerDAO, String username) {
        System.out.println("Please enter a username for your new account:");
        String newUsername = scanner.nextLine();

        // Check if the username already exists
        if (customerDAO.usernameExists(newUsername)) {
            System.out.println("Username already exists. Please choose another one.");
            createAccount(scanner, customerDAO, null); // Recursively call createAccount until a unique username is provided
        } else {
            // Add the new customer to the database
            customerDAO.addCustomer(newUsername);
            System.out.println("Account created successfully!");
            cart(newUsername, customerDAO);
            product();

            // Log in with the newly created account
            int customerId = customerDAO.fetchCustomerIdByUsername(newUsername);
            updateCart(scanner, customerId);
        }
    }

    public static void product() {
        Product product1 = new Product("Laptop", 74000.00);
        Product product2 = new Product("Airpods3", 18000.00);

        ProductDAO productDAO = new ProductDAO();
        String name1 = product1.getName();
        String name2 = product2.getName();
        double price1 = product1.getPrice();
        double price2 = product2.getPrice();
        productDAO.addProduct(name1, price1);
        productDAO.addProduct(name2, price2);

        ProductDisplay.displayAvailableProducts(name1, name2);
    }

    public static void cart(String username, CustomerDAO customerDAO) {
        if (customerDAO.usernameExists(username)) {
            int customerId = customerDAO.fetchCustomerIdByUsername(username);
            CartDAO cartDAO = new CartDAO();
            cartDAO.addCart(customerId);
        } else {
            System.out.println("Username does not exist. Cannot create cart.");
        }
    }

    public static void updateCart(Scanner scanner, int customerId) {
    CartItemDAO cartItemDAO = new CartItemDAO();
    ProductDAO productDAO = new ProductDAO();
    
    double totalBill = 0.0;

    while (true) {
        System.out.println("Do you want to add or remove items from the cart? (1 for add, 2 for remove, 3 to finish and pay)");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        
        if (choice == 3) {
            // Generate and display the bill
            System.out.println("Your Bill:");
            System.out.println("Product\t\tPrice\t\tQuantity\tTotal");
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomerId(customerId);
            for (CartItem cartItem : cartItems) {
                String productName = productDAO.getProductNameById(cartItem.getProductId());
                double price = productDAO.getProductPriceById(cartItem.getProductId());
                int quantity = cartItem.getQuantity();
                double total = price * quantity;
                totalBill += total;
                System.out.printf("%s\t\tRs.%.2f\t\t%d\t\tRs.%.2f\n", productName, price, quantity, total);
            }
            System.out.println("Total Bill: Rs." + String.format("%.2f", totalBill));
            
            // Ask user to pay or go back
            System.out.println("Do you want to pay now? (yes/no)");
            String payChoice = scanner.nextLine();
            if (payChoice.equalsIgnoreCase("yes")) {
                // Clear the cart and cart items
                cartItemDAO.clearCart(customerId);
                System.out.println("Payment successful. Thank you for shopping with us!");
                break;
            } else {
                System.out.println("Your cart has not been cleared. You can continue shopping.");
            }
        } else if (choice == 1) {
            System.out.println("Enter the product name to add to cart:");
            String productName = scanner.nextLine().trim();
            System.out.println("Enter the quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            // Add the item to the cart
            cartItemDAO.addItemToCart(customerId, productName, quantity);
        } else if (choice == 2) {
            System.out.println("Enter the product name to remove from cart:");
            String productName = scanner.nextLine().trim();
            System.out.println("Enter the quantity to remove:");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            // Remove the item from the cart
            cartItemDAO.removeItemFromCart(customerId, productName, quantity);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
}



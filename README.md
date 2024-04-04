# Cart-Service-System
Simple system to add, remove and sign out items from cart.

## Overview

This project is a simple backend of a shopping website that allows users to browse available products, add them to their cart, and proceed to checkout. It includes functionalities such as user authentication, cart management, and product display.

## Database Schema

The project uses a relational database to store user information, products, carts, and cart items. Here is an overview of the database schema:

### Customer Table

- **customer_id**: Unique identifier for each customer
- **username**: Username of the customer

### Product Table

- **product_id**: Unique identifier for each product
- **name**: Name of the product
- **price**: Price of the product

### Cart Table

- **cart_id**: Unique identifier for each cart
- **customer_id**: Foreign key referencing the customer who owns the cart

### Cartitem Table

- **cart_item_id**: Unique identifier for each cart item
- **cart_id**: Foreign key referencing the cart to which the item belongs
- **product_id**: Foreign key referencing the product in the cart item
- **quantity**: Quantity of the product in the cart item

## Technologies Used

- Java
- MySQL
- JDBC

## Project Structure

The project is organized into several packages:

- **DAO_files**: Contains classes responsible for database access and manipulation.
- **domain_classes**: Contains classes representing domain models such as customers, products, carts, and cart items.

## Setup Instructions

1. Clone the repository to your local machine.
2. Set up the MySQL database using the provided SQL scripts.
3. Update the database connection settings in the DAO classes.
4. Compile and run the application.

## Usage

- Run the main class to start the shopping website.
- Follow the prompts to log in or create an account.
- Browse available products, add them to your cart, and proceed to checkout.




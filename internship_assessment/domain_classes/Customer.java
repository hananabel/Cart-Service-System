class Customer {
    private int customerId;
    private String username;

    public Customer (String username) {
        this.username = username;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}